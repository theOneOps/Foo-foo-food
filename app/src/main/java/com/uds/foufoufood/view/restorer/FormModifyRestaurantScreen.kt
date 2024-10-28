package com.uds.foufoufood.view.restorer

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uds.foufoufood.Firebase_management.FirebaseInstance
import com.uds.foufoufood.Firebase_management.FirebaseInstance.downloadAndCompressImageFromUrl
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.viewmodel.RestaurantViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
// todo modifier aspect form
fun FormModifyRestaurantScreen(
    restaurant: Restaurant,
    restaurantViewModel: RestaurantViewModel
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

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Text(text = "Modifier le Restaurant", style = MaterialTheme.typography.headlineLarge) }

        item {
            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Nom du restaurant") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = specialityState.value,
                onValueChange = { specialityState.value = it },
                label = { Text("Spécialité") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = phoneState.value,
                onValueChange = { phoneState.value = it },
                label = { Text("Téléphone") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = openingHoursState.value,
                onValueChange = { openingHoursState.value = it },
                label = { Text("Horaires d'ouverture") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = imageUrlState.value,
                onValueChange = {},
                label = { Text("Select an image") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { launcher.launch("image/*") }) {
                        Icon(Icons.Filled.AddAPhoto, contentDescription = "Select Image")
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
            OutlinedTextField(
                value = numberState.value,
                onValueChange = { numberState.value = it },
                label = { Text("Numéro") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = streetState.value,
                onValueChange = { streetState.value = it },
                label = { Text("Rue") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = cityState.value,
                onValueChange = { cityState.value = it },
                label = { Text("Ville") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = zipCodeState.value,
                onValueChange = { zipCodeState.value = it },
                label = { Text("Code postal") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = stateState.value,
                onValueChange = { stateState.value = it },
                label = { Text("État / Région") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = countryState.value,
                onValueChange = { countryState.value = it },
                label = { Text("Pays") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            // Bouton pour sauvegarder les modifications
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Création d'un objet Address avec les valeurs mises à jour
                    val updatedAddress = Address(
                        number = numberState.value.toIntOrNull(),
                        street = streetState.value,
                        city = cityState.value,
                        zipCode = zipCodeState.value,
                        state = stateState.value,
                        country = countryState.value
                    )

                    // Création d'un nouvel objet Restaurant avec les valeurs mises à jour
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
                        reviews = restaurant.reviews, // conserver les avis actuels
                        items = restaurant.items, // conserver les items actuels
                    )

                    // Appel de la fonction de mise à jour avec l'id du restaurant et l'objet mis à jour
                    restaurantViewModel.updateRestaurant(restaurant._id, updatedRestaurant)
                    Toast.makeText(context, "Restaurant bien modifié", Toast.LENGTH_SHORT).show()
                },
            ) {
                Text("Sauvegarder")
            }
        }
    }
}
