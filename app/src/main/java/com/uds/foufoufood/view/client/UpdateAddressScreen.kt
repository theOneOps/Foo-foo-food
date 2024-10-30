package com.uds.foufoufood.view.client

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.ui.component.ValidateButton
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun UpdateAddressScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val address = userViewModel.user.value?.address
    var number by remember { mutableStateOf(address?.number) }
    var street by remember { mutableStateOf(address?.street ?: "") }
    var zipCode by remember { mutableStateOf(address?.zipCode ?: "") }
    var city by remember { mutableStateOf(address?.city ?: "") }
    var state by remember { mutableStateOf(address?.state ?: "") }
    var country by remember { mutableStateOf(address?.country ?: "") }

    val updateAddressSuccess by userViewModel.updateAddressSuccess.observeAsState()

    Log.d("UpdateAddressScreen", "Address : $address")
    Log.d("UpdateAddressScreen", "$updateAddressSuccess")

    LaunchedEffect(updateAddressSuccess) {
        if (updateAddressSuccess == true) {
            Toast.makeText(context, "Adresse mise à jour avec succès", Toast.LENGTH_SHORT).show()
            userViewModel.resetStatus()
            navController.navigate(Screen.Address.route)
        } else if (updateAddressSuccess == false) {
            Toast.makeText(context, "Échec de la mise à jour de l'adresse", Toast.LENGTH_SHORT).show()
        }
    }

    val errorMessage by userViewModel.errorMessage.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        TitlePage(label = stringResource(id = R.string.update_delivery_address))

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // Appliquer Modifier.weight dans le Row ici
            AddressTextField(
                value = number?.toString() ?: "",
                placeholder = "N°",
                onValueChange = {
                    number = it.toIntOrNull()
                },
                modifier = Modifier.weight(1f) // Appliquer le weight dans le Row
            )

            Spacer(modifier = Modifier.width(11.dp))

            AddressTextField(
                value = street,
                placeholder = "Nom de rue",
                onValueChange = {
                    street = it
                },
                modifier = Modifier.weight(3f) // Appliquer un autre poids
            )
        }

        Spacer(modifier = Modifier.height(11.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddressTextField(
                value = zipCode,
                placeholder = "Code postal",
                onValueChange = {
                    zipCode = it
                },
                modifier = Modifier.weight(1.3f) // Appliquer le weight
            )

            Spacer(modifier = Modifier.width(11.dp))

            AddressTextField(
                value = city,
                placeholder = "Ville",
                onValueChange = {
                    city = it
                },
                modifier = Modifier.weight(1.7f) // Appliquer le weight ici aussi
            )
        }

        Spacer(modifier = Modifier.height(11.dp))

        AddressTextField(
            value = state,
            placeholder = "Région",
            onValueChange = {
                state = it
            }
        )

        Spacer(modifier = Modifier.height(11.dp))

        AddressTextField(
            value = country,
            placeholder = "Pays",
            onValueChange = {
                country = it
            }
        )

        Spacer(modifier = Modifier.height(25.dp))

        ValidateButton(
            label = stringResource(id = R.string.validate),
            onClick = {
                if (number == null) {
                    Toast.makeText(
                        context,
                        "Veuillez entrer un numéro valide",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (street.isEmpty() || city.isEmpty() || state.isEmpty() || zipCode.isEmpty() || country.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Veuillez remplir tous les champs",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (number != null && number.toString().isNotEmpty() && street.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && zipCode.isNotEmpty() && country.isNotEmpty()) {
                    userViewModel.updateAddress(
                        number = number!!,
                        street = street,
                        city = city,
                        zipCode = zipCode,
                        state = state,
                        country = country
                    )
                }
            }
        )
    }

    // Afficher les erreurs de connexion si elles existent
    errorMessage?.let {
        Text(text = "$it", color = Color.Red, modifier = Modifier.padding(16.dp))
    }
}

fun validateAddress(
    number: Number,
    street: String,
    city: String,
    state: String,
    zipCode: String,
    country: String
): Boolean {
    return number.toString()
        .isNotEmpty() && street.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && zipCode.isNotEmpty() && country.isNotEmpty()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier // Accepter un Modifier externe pour personnalisation
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        placeholder = {
            Text(text = placeholder, color = Color.Gray)
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = colorResource(id = R.color.black),
            unfocusedTextColor = Color.Gray,
            focusedIndicatorColor = colorResource(id = R.color.orange),
            unfocusedIndicatorColor = Color.Gray,
            errorIndicatorColor = Color.Red,
            cursorColor = colorResource(id = R.color.orange),
            containerColor = colorResource(id = R.color.white),
            errorContainerColor = colorResource(id = R.color.white_grey),
        ),
        textStyle = TextStyle(fontSize = 16.sp)
    )
}