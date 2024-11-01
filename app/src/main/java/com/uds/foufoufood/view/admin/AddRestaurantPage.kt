package com.uds.foufoufood.view.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Restaurant
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRestaurantPage(
    navController: NavController, onRestaurantAdded: (Restaurant) -> Unit
) {
    var restaurantName by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Ajouter un restaurant") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = restaurantName,
                onValueChange = { restaurantName = it },
                label = { Text("Nom du restaurant") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (restaurantName.isNotEmpty()) {
                        val newAddress = Address(
                            street = "Street", number = 99, country = "Canada", city = "Sherbrooke"
                        )
                        // Créer un nouvel objet restaurant et le passer à la fonction de callback
                        val newRestaurant = Restaurant(
                            name = restaurantName,
                            newAddress,
                            speciality = "",
                            phone = "+0123456789",
                            openingHours = "09:00 - 17:00",
                            items = listOf(),
                            rating = 0.0,
                            imageUrl = "",
                            userId = "",
                            _id = UUID.randomUUID().toString(),
                        )
                        onRestaurantAdded(newRestaurant)
                        navController.popBackStack() // Retour à la liste des restaurants après l'ajout
                    }
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Ajouter")
            }
        }
    }
}
