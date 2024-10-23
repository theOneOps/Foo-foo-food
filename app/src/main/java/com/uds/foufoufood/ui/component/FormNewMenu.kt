package com.uds.foufoufood.ui.component

import android.net.Uri
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
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.viewmodel.MenuViewModel

@Composable
fun FormNewMenu(restaurant: Restaurant, menuViewModel: MenuViewModel) {
    val nameState = remember { mutableStateOf("") }
    val descriptorState = remember { mutableStateOf("") }
    val priceState = remember { mutableStateOf("") }
    val categoryState = remember { mutableStateOf("") }
    val imageState = remember { mutableStateOf("") }

    val context = LocalContext.current
    val token = getToken(context) ?: return

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                imageState.value = it.toString() // Conservez l'URI de l'image
            }
        }
    )

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

        // Champ pour l'image (URL ou chemin)
        OutlinedTextField(
            value = imageState.value,
            onValueChange = { imageState.value = it },
            label = { Text("Image URL or select an image") },
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
                // Créer l'objet Menu et le passer à la fonction onSubmit
                val newMenu = restaurant.restaurantId?.let {
                    Menu(
                        name = nameState.value,
                        description = descriptorState.value,
                        price = price,
                        category = categoryState.value,
                        image = if (imageState.value.isNotEmpty()) imageState.value else "",
                        restaurantId = it,
                        _id = "ABC",
                    )
                }

                // appel au viewModel pour post le menu sur le serveur
                menuViewModel.createMenu(token, nameState.value,descriptorState.value,
                    price, restaurant.restaurantId, categoryState.value)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ajouter le menu")
        }
    }
}