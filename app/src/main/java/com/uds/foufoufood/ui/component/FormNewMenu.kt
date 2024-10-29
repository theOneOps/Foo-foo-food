package com.uds.foufoufood.ui.component

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uds.foufoufood.Firebase_management.FirebaseInstance
import com.uds.foufoufood.Firebase_management.FirebaseInstance.downloadAndCompressImageFromUrl
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.viewmodel.MenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormNewMenu(restaurant: Restaurant, menuViewModel: MenuViewModel, navController: NavController) {
    val nameState = remember { mutableStateOf("") }
    val descriptorState = remember { mutableStateOf("") }
    val priceState = remember { mutableStateOf("") }
    val categoryState = remember { mutableStateOf("") }
    val imageState = remember { mutableStateOf("") }
    val ingredientsState = remember { mutableStateOf(listOf("")) }

    val allIngredientsFilled = ingredientsState.value.all { it.isNotEmpty() }

    val context = LocalContext.current
    val token = getToken(context) ?: return

    val scrollState = rememberLazyListState()

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
                            imageState.value = downloadUrl.toString()
                            println("L'image a été téléchargée : ${imageState.value}")
                            Log.d("FormNewMenu", imageState.value)
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
                TitlePage(label = "Ajouter \nun menu")
            }

            item {
                TextFieldWithError(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    label = "Nom",
                    errorMessage = "Le nom du menu ne peut pas être vide",
                    isValid = { it.isNotEmpty() }
                )
            }

            item {
                TextFieldWithError(
                    value = descriptorState.value,
                    onValueChange = { descriptorState.value = it },
                    label = "Description",
                    errorMessage = "La description ne peut pas être vide",
                    isValid = { it.isNotEmpty() }
                )
            }

            item {
                TextFieldWithError(
                    value = priceState.value,
                    onValueChange = { priceState.value = it },
                    label = "Prix",
                    errorMessage = "Le prix ne peut pas être vide",
                    isValid = {
                        it.isNotEmpty() && it.toDoubleOrNull() != null
                    },
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                TextFieldWithError(
                    value = categoryState.value,
                    onValueChange = { categoryState.value = it },
                    label = "Catégorie",
                    errorMessage = "",
                    isValid = { true }
                )
            }

            // Gestion dynamique des ingrédients
            item {
                Text(
                    text = "Ingrédients : ",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
                    modifier = Modifier.padding(vertical = 8.dp))
                ingredientsState.value.forEachIndexed { index, ingredient ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            value = ingredient,
                            onValueChange = { newIngredient ->
                                val ingredients = ingredientsState.value.toMutableList()
                                ingredients[index] = newIngredient
                                ingredientsState.value = ingredients
                            },
                            label = {
                                Text(
                                    text = "Ingrédient ${index + 1}",
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
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                ingredientsState.value = ingredientsState.value.toMutableList().apply { removeAt(index) }
                            },
                            enabled = ingredientsState.value.size > 1 // Désactivez le bouton "-" si un seul champ reste
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Supprimer")
                        }
                    }
                }
                IconButton(
                    onClick = { ingredientsState.value = ingredientsState.value.toMutableList().apply { add("") } },
                    enabled = allIngredientsFilled
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter un ingrédient")
                }
            }

            item {
                TextField(
                    value = imageState.value,
                    onValueChange = { imageState.value = it },
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

            if (imageState.value.isNotEmpty()) {
                item {
                    AsyncImage(
                        model = imageState.value,
                        contentDescription = "Image du restaurant",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(vertical = 16.dp)
                    )
                }
            }
            else{
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (nameState.value == "" || descriptorState.value == "") {
                                Toast.makeText(
                                    context,
                                    "Veuillez remplir tous les champs obligatoires",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (priceState.value == "" || priceState.value.toDoubleOrNull() == null) {
                                Toast.makeText(
                                    context, "Le prix doit être un nombre",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (!allIngredientsFilled) {
                                Toast.makeText(
                                    context,
                                    "Veuillez remplir tous les champs d'ingrédients",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            // Conversion du prix en Double avant la soumission
                            val price = priceState.value.toDoubleOrNull() ?: 0.0

                            // Vérifie si une image a été uploadée et l'URL est disponible
                            val imageUri = imageState.value
                            if (imageUri.isNotEmpty()) {
                                // Appel au ViewModel pour créer le menu avec l'URL de l'image déjà uploadée
                                menuViewModel.createMenu(
                                    token, nameState.value, descriptorState.value,
                                    price, restaurant._id, categoryState.value, imageUri, ingredientsState.value
                                )
                                Toast.makeText(
                                    context,
                                    "Menu créé avec succès",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                // Si aucune image n'a été uploadée, appeler directement createMenu sans l'URL
                                menuViewModel.createMenu(
                                    token, nameState.value, descriptorState.value,
                                    price, restaurant._id, categoryState.value, "", ingredientsState.value
                                )
                                Toast.makeText(
                                    context,
                                    "Menu créé avec succès",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
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