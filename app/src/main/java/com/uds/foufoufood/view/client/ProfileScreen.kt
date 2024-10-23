package com.uds.foufoufood.view.client

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.PasswordTextField
import com.uds.foufoufood.view.auth.isValidEmail
import com.uds.foufoufood.view.auth.isValidPassword
import com.uds.foufoufood.viewmodel.UserViewModel


@Composable
fun ProfileScreen(navController: NavController, userViewModel: UserViewModel) {
    // Extraire les données de l'utilisateur connecté
    val name = userViewModel.user.value?.name ?: ""
    val email = userViewModel.user.value?.email ?: ""
    val role = userViewModel.user.value?.role
    val address = userViewModel.user.value?.address
    val hasAddress = address != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Spacer(modifier = Modifier.height(8.dp))

        ProfileName(name = name)

        Spacer(modifier = Modifier.height(16.dp))

        if (role != null) {
            ProfileType(role = role)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Gestion de l'email
        ProfileEmailSection(
            initialEmail = email,
            onSaveClick = { newEmail ->
                if (newEmail != email) {
                    if (isValidEmail(newEmail)) {
                        // Mettre à jour l'email de l'utilisateur
                        userViewModel.updateEmail(email, newEmail)
                        navController.navigate("verify_code/${newEmail}")
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Veuillez saisir un email valide",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else {
                    Toast.makeText(
                        navController.context,
                        "Aucune modification apportée",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfilePasswordSection(
            initialPassword = "Cliquez sur l'icone pour modifier",
            onSaveClick = { newPassword ->
                if (isValidPassword(newPassword)) {
                    userViewModel.updatePassword(newPassword)
                    navController.navigate(Screen.Profile.route)
                } else {
                    Toast.makeText(
                        navController.context,
                        R.string.password_constraint,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        if (hasAddress) {
            ProfileExistingAddress( /* TODO : Afficher l'adresse existante */)
        } else {
            NoAddressSection(onAddAddressClick = { /* TODO: Ajouter une adresse */ })
        }
    }
}

@Composable
fun ProfileName(name: String) {
    Text(
        text = name,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFFFA500) // Couleur orange
    )
}

@Composable
fun ProfileType(role: String) {
    Text(
        text = role,
        fontSize = 16.sp,
        color = Color(0xFF8B4513) // Couleur marron
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEmailSection(
    initialEmail: String,
    onSaveClick: (String) -> Unit
) {
    var isEditable by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf(initialEmail) }
    var isError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.email), // Libellé pour "Email"
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(end = 10.dp)
            )

            if (!isEditable) {
                // Bouton pour activer l'édition de l'email
                IconButton(onClick = {
                    isEditable = true // Passer en mode édition
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.pen_square_icon), // Icône pour modifier
                        contentDescription = "Edit Email",
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                // Bouton pour sauvegarder l'email modifié
                IconButton(onClick = {
                    isEditable = false // Désactiver le mode édition
                    onSaveClick(email) // Appeler la fonction de sauvegarde
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = "Save Email",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Champ de texte pour afficher/modifier l'email
        TextField(
            value = email, // Email actuel de l'utilisateur
            onValueChange = {
                email = it
                isError = !isValidEmail(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = isEditable, // Activer/désactiver l'édition
            placeholder = {
                Text(text = stringResource(id = R.string.email_example), color = Color.Gray)
            },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(stringResource(id = R.string.enter_valid_email), color = Color.Red)
                }
            },
            textStyle = TextStyle(fontSize = 16.sp),
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
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePasswordSection(
    initialPassword: String,
    onSaveClick: (String) -> Unit
) {
    var isEditable by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("Cliquez sur l'icone pour modifier") }
    var isError by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.password), // Libellé pour "Mot de Passe"
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(end = 10.dp)
            )

            if (!isEditable) {
                // Bouton pour activer l'édition du mot de passe
                IconButton(onClick = {
                    isEditable = true // Passer en mode édition
                    password = ""
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.pen_square_icon), // Icône pour modifier
                        contentDescription = "Edit Password",
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                // Bouton pour sauvegarder le mot de passe modifié
                IconButton(onClick = {
                    if (!isError) {
                        isEditable = false // Désactiver le mode édition
                        onSaveClick(password) // Appeler la fonction de sauvegarde
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = "Save Password",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Champ de texte pour afficher/modifier le mot de passe
        TextField(
            value = password,
            onValueChange = {
                password = it
                isError = !isValidPassword(it) // Valider le mot de passe
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
            label = {
                Text(
                    text = stringResource(id = R.string.password),
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.sofiapro_regular))
                )
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(stringResource(R.string.password_constraint), color = Color.Red)
                }
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Image(
                        painter = painterResource(id = if (isPasswordVisible) R.drawable.visibility_off else R.drawable.visibility),
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            enabled = isEditable, // Activer ou désactiver l'édition
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


@Composable
fun ProfileExistingAddress() {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Appliquer Modifier.weight dans le Row ici
            AddressTextField(
                value = "123",
                placeholder = "Numéro de rue",
                onValueChange = { /* Gérer le changement */ },
                modifier = Modifier.weight(1f) // Appliquer le weight dans le Row
            )

            Spacer(modifier = Modifier.width(10.dp))

            AddressTextField(
                value = "Rue de la Paix",
                placeholder = "Nom de rue",
                onValueChange = { /* Gérer le changement */ },
                modifier = Modifier.weight(3f) // Appliquer un autre poids
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddressTextField(
                value = "75000",
                placeholder = "Code postal",
                onValueChange = { /* Gérer le changement */ },
                modifier = Modifier.weight(1.3f) // Appliquer le weight
            )

            Spacer(modifier = Modifier.width(10.dp))

            AddressTextField(
                value = "Paris",
                placeholder = "Ville",
                onValueChange = { /* Gérer le changement */ },
                modifier = Modifier.weight(1.7f) // Appliquer le weight ici aussi
            )
        }
    }
}

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
            .height(65.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(start = 25.dp),
        placeholder = {
            Text(text = placeholder, color = Color.Gray)
        },
        textStyle = TextStyle(fontSize = 16.sp)
    )
}

@Composable
fun NoAddressSection(onAddAddressClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.no_address),
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Button(
            onClick = { onAddAddressClick() },
            modifier = Modifier
                .height(60.dp)
                .padding(horizontal = 50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange))
        ) {
            Text(
                text = stringResource(id = R.string.add_address),
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}