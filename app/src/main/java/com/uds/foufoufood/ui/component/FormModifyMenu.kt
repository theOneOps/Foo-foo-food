package com.uds.foufoufood.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.data_class.model.Menu

@Composable
fun FormModifyMenu(menu: Menu, onUpdate: (Menu) -> Unit) {
    // State pour les champs du formulaire
    val nameState = remember { mutableStateOf(menu.name) }
    val descriptorState = remember { mutableStateOf(menu.description) }
    val priceState = remember { mutableStateOf(menu.price.toString()) }
    val categoryState = remember { mutableStateOf(menu.category) }
    val imageState = remember { mutableStateOf(menu.image) }

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

        // Champ pour l'image
        OutlinedTextField(
            value = imageState.value ?: "",
            onValueChange = { imageState.value = it },
            label = { Text("URL de l'image") },
            modifier = Modifier.fillMaxWidth()
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
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Sauvegarder")
        }
    }
}
