package com.uds.foufoufood.ui.component

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.Firebase_management.FirebaseInstance
import com.uds.foufoufood.Firebase_management.FirebaseInstance.downloadAndCompressImageFromUrl
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.viewmodel.MenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FormNewMenu(restaurant: Restaurant, menuViewModel: MenuViewModel) {
    val nameState = remember { mutableStateOf("") }
    val descriptorState = remember { mutableStateOf("") }
    val priceState = remember { mutableStateOf("") }
    val categoryState = remember { mutableStateOf("") }
    val imageState = remember { mutableStateOf("") }

    val context = LocalContext.current
    val token = getToken(context) ?: return

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Form to add new Menu",
            style = TextStyle(
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
            )
        )
        Spacer(Modifier.fillMaxHeight(0.02f))
        // Champ pour le nom
        TextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Nom du menu") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Champ pour la description
        TextField(
            value = descriptorState.value,
            onValueChange = { descriptorState.value = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Champ pour le prix (sous forme de texte, à convertir en double)
        TextField(
            value = priceState.value,
            onValueChange = { priceState.value = it },
            label = { Text("Prix") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Champ pour la catégorie
        TextField(
            value = categoryState.value,
            onValueChange = { categoryState.value = it },
            label = { Text("Catégorie") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                                "images/${restaurant.userId}/${restaurant.restaurantId}" +
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

// OutlinedTextField pour l'image
        OutlinedTextField(
            value = imageState.value,
            onValueChange = { },
            label = { Text("Select an image") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(Icons.Filled.AddAPhoto, contentDescription = "Select Image")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton pour soumettre le formulaire
        Button(
            onClick = {
                // Conversion du prix en Double avant la soumission
                val price = priceState.value.toDoubleOrNull() ?: 0.0

                // Vérifie si une image a été uploadée et l'URL est disponible
                val imageUri = imageState.value
                if (imageUri.isNotEmpty()) {
                    // Appel au ViewModel pour créer le menu avec l'URL de l'image déjà uploadée
                    menuViewModel.createMenu(
                        token, nameState.value, descriptorState.value,
                        price, restaurant.restaurantId, categoryState.value, imageUri
                    )
                    Toast.makeText(context, "nouveau menu bien créé", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Si aucune image n'a été uploadée, appeler directement createMenu sans l'URL
                    menuViewModel.createMenu(
                        token, nameState.value, descriptorState.value,
                        price, restaurant.restaurantId, categoryState.value, ""
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ajouter le menu")
        }
    }
}