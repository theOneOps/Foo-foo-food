package com.uds.foufoufood.view.restorer

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uds.foufoufood.Firebase_management.FirebaseInstance
import com.uds.foufoufood.Firebase_management.FirebaseInstance.downloadAndCompressImageFromUrl
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.ui.component.BackButton
import com.uds.foufoufood.ui.component.TextFieldWithError
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.ui.component.ValidateButton
import com.uds.foufoufood.viewmodel.RestaurantViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormModifyRestaurantScreen(
    restaurant: Restaurant,
    restaurantViewModel: RestaurantViewModel,
    navController: NavController
) {
    // Récupération du restaurant à modifier et états des champs (identique à la définition précédente)
    val nameState = remember { mutableStateOf(restaurant.name) }
    val specialityState = remember { mutableStateOf(restaurant.speciality) }
    val phoneState = remember { mutableStateOf(restaurant.phone) }
    val openingHoursState = remember { mutableStateOf(restaurant.openingHours) }
    val imageUrlState = remember { mutableStateOf(restaurant.imageUrl) }

    // États pour les champs d'adresse
    val numberState = remember { mutableStateOf(restaurant.address.number.toString()) }
    val streetState = remember { mutableStateOf(restaurant.address.street ?: "") }
    val cityState = remember { mutableStateOf(restaurant.address.city ?: "") }
    val zipCodeState = remember { mutableStateOf(restaurant.address.zipCode ?: "") }
    val stateState = remember { mutableStateOf(restaurant.address.state ?: "") }
    val countryState = remember { mutableStateOf(restaurant.address.country ?: "") }

    val scrollState = rememberLazyListState()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Convertir le Uri en String (ici, en supposant qu'il s'agit d'une URL valide)
            val imageUrl = it.toString() // Assurez-vous que c'est une URL valide

            // Appeler la fonction pour télécharger et compresser l'image
            CoroutineScope(Dispatchers.Main).launch {
                val compressedImage =
                    downloadAndCompressImageFromUrl(imageUrl, context)

                if (compressedImage != null) {
                    // Référence Firebase où l'image sera stockée
                    val storageRef =
                        FirebaseInstance.storageRef.child(
                            "images/${restaurant.userId}/${restaurant._id}" +
                                    "${System.currentTimeMillis()}.webp"
                        )

                    // Envoie l'image compressée sur Firebase
                    val uploadTask = storageRef.putBytes(compressedImage)

                    // Gérer le succès ou l'échec de l'envoi
                    uploadTask.addOnSuccessListener {
                        // Récupérer l'URL de téléchargement une fois l'image envoyée
                        storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            // Mets à jour imageState avec l'URL de l'image sur Firebase
                            imageUrlState.value = downloadUrl.toString()
                            println("L'image a été téléchargée : ${imageUrlState.value}")
                            Log.d("FormNewMenu", imageUrlState.value)
                        }
                    }.addOnFailureListener { e ->
                        Log.e("Firebase", "Échec de l'upload de l'image : ${e.message}", e)
                    }
                } else {
                    Log.e(
                        "Image",
                        "Erreur lors du téléchargement ou de la compression de l'image."
                    )
                }
            }
        }
    }

    Column (modifier = Modifier
        .fillMaxSize()
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                BackButton(navController = navController)
                TitlePage(label = "Modifier \nle restaurant")
            }

            item {
                TextFieldWithError(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    label = "Nom du restaurant",
                    errorMessage = "Le nom du restaurant est requis",
                    isValid = { it.isNotEmpty() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TextFieldWithError(
                    value = specialityState.value,
                    onValueChange = { specialityState.value = it },
                    label = "Spécialité",
                    errorMessage = "",
                    isValid = { true },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TextFieldWithError(
                    value = phoneState.value,
                    onValueChange = { phoneState.value = it },
                    label = "Téléphone",
                    errorMessage = "Le numéro de téléphone est requis",
                    isValid = { isValidPhoneNumber(it) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Phone
                )
            }

            item {
                TextFieldWithError(
                    value = openingHoursState.value,
                    onValueChange = { openingHoursState.value = it },
                    label = "Horaires d'ouverture",
                    errorMessage = "Les horaires d'ouverture sont requis et doivent être au format HH:MM - HH:MM",
                    isValid = { isValidHours(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TextField(
                    value = imageUrlState.value,
                    onValueChange = { imageUrlState.value = it },
                    label = {
                        Text(
                            "URL de l'image",
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.sofiapro_regular))
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = colorResource(id = R.color.black),
                        unfocusedTextColor = colorResource(id = R.color.black),
                        focusedIndicatorColor = colorResource(id = R.color.orange),
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red,
                        cursorColor = colorResource(id = R.color.orange),
                        containerColor = colorResource(id = R.color.grey_bg_alpha),
                        errorContainerColor = colorResource(id = R.color.white_grey),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { launcher.launch("image/*") }) {
                            Icon(
                                Icons.Filled.AddAPhoto,
                                contentDescription = "Sélectionner une image"
                            )
                        }
                    }
                )
            }

            item {
                AsyncImage(
                    model = imageUrlState.value,
                    contentDescription = "Image du restaurant",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 16.dp)
                )
            }

            // Champs pour l'adresse
            item {
                TextFieldWithError(
                    value = numberState.value,
                    onValueChange = { numberState.value = it },
                    label = "Numéro",
                    errorMessage = "Le numéro de rue est requis",
                    isValid = { it.isNotEmpty() && it.toIntOrNull() != null },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                TextFieldWithError(
                    value = streetState.value,
                    onValueChange = { streetState.value = it },
                    label = "Rue",
                    errorMessage = "Le nom de la rue est requis",
                    isValid = { it.isNotEmpty() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TextFieldWithError(
                    value = cityState.value,
                    onValueChange = { cityState.value = it },
                    label = "Ville",
                    errorMessage = "Le nom de la ville est requis",
                    isValid = { it.isNotEmpty() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TextFieldWithError(
                    value = stateState.value,
                    onValueChange = { stateState.value = it },
                    label = "Etat / région",
                    errorMessage = "",
                    isValid = { true },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                TextFieldWithError(
                    value = zipCodeState.value,
                    onValueChange = { zipCodeState.value = it },
                    label = "Code postal",
                    errorMessage = "",
                    isValid = { true },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TextFieldWithError(
                    value = countryState.value,
                    onValueChange = { countryState.value = it },
                    label = "Pays",
                    errorMessage = "Le pays est requis",
                    isValid = { it.isNotEmpty() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            // véfier si tous les champs sont valides

                            if (nameState.value == "" || phoneState.value == "" ||
                                openingHoursState.value == "" || numberState.value == "" || streetState.value == "" ||
                                cityState.value == "" || countryState.value == "") {
                                Toast.makeText(context, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (!isValidPhoneNumber(phoneState.value)) {
                                Toast.makeText(context, "Le numéro de téléphone doit être au format +33XXXXXXXXX", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (!isValidHours(openingHoursState.value)) {
                                Toast.makeText(context, "Les horaires d'ouverture doivent être au format HH:MM - HH:MM", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (numberState.value.toIntOrNull() == null) {
                                Toast.makeText(context, "Le numéro de rue doit être un nombre", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val updatedAddress = Address(
                                number = numberState.value.toIntOrNull(),
                                street = streetState.value,
                                city = cityState.value,
                                zipCode = zipCodeState.value,
                                state = stateState.value,
                                country = countryState.value
                            )

                            val updatedRestaurant = Restaurant(
                                name = nameState.value,
                                address = updatedAddress,
                                speciality = specialityState.value,
                                phone = phoneState.value,
                                openingHours = openingHoursState.value,
                                imageUrl = imageUrlState.value,
                                userId = restaurant.userId,
                                _id = restaurant._id,
                                rating = restaurant.rating,
                                reviews = restaurant.reviews,
                                items = restaurant.items,
                            )

                            restaurantViewModel.updateRestaurant(restaurant._id, updatedRestaurant)
                            Toast.makeText(context, "Restaurant modifié avec succès", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .height(60.dp)
                            .padding(horizontal = 50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange)),
                    ) {
                        Text(
                            text = "Sauvegarder",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.sofiapro_semibold))
                        )
                    }
                }
            }
        }
    }
}

fun isValidHours(hours: String): Boolean {
    // regex pour vérifier si les heures sont valides
    val regex = Regex("([0-1]?[0-9]|2[0-3]):[0-5][0-9] - ([0-1]?[0-9]|2[0-3]):[0-5][0-9]")
    return hours.isNotEmpty() && regex.matches(hours)
}

fun isValidPhoneNumber(phone: String): Boolean {
    // regex pour vérifier si le numéro de téléphone est valide
    val regex = Regex("^\\+?(\\d{1,3})?[-.\\s]?(\\(?\\d{1,4}\\)?)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}\$")
    return phone.isNotEmpty() && regex.matches(phone)
}