package com.uds.foufoufood.ui.component

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.Firebase_management.FirebaseInstance
import com.uds.foufoufood.Firebase_management.FirebaseInstance.downloadAndCompressImageFromUrl
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.viewmodel.MenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FormModifyMenu(menuViewModel: MenuViewModel,menu: Menu, onUpdate: (Menu) -> Unit) {
    // State pour les champs du formulaire
    val nameState = remember { mutableStateOf(menu.name) }
    val descriptorState = remember { mutableStateOf(menu.description) }
    val priceState = remember { mutableStateOf(menu.price.toString()) }
    val categoryState = remember { mutableStateOf(menu.category) }
    val imageState = remember { mutableStateOf(menu.image) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Modifier le Menu",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Champ pour le nom
        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Nom du menu") },
            modifier = Modifier.fillMaxWidth()
        )

        // Champ pour la description
        OutlinedTextField(
            value = descriptorState.value,
            onValueChange = { descriptorState.value = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        // Champ pour le prix
        OutlinedTextField(
            value = priceState.value,
            onValueChange = { priceState.value = it },
            label = { Text("Prix") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Champ pour la catégorie
        OutlinedTextField(
            value = categoryState.value,
            onValueChange = { categoryState.value = it },
            label = { Text("Catégorie") },
            modifier = Modifier.fillMaxWidth()
        )

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
                                "images/${menu._id}/${menu.restaurantId}" +
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

        // Champ pour l'image
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

        // Bouton pour sauvegarder les modifications
        Button(
            onClick = {
                // Créer un nouvel objet Menu avec les valeurs mises à jour
                val updatedMenu = Menu(
                    menu._id,
                    name = nameState.value,
                    description = descriptorState.value,
                    price = priceState.value.toDoubleOrNull() ?: menu.price,
                    category = categoryState.value,
                    menu.restaurantId,
                    image = imageState.value
                )
                onUpdate(updatedMenu) // Appeler la fonction de mise à jour


                Toast.makeText(context, "menu bien modifié", Toast.LENGTH_SHORT)
                    .show()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Sauvegarder")
        }
    }
}
