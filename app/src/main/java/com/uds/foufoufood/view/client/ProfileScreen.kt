package com.uds.foufoufood.view.client

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.view.auth.isValidEmail
import com.uds.foufoufood.view.auth.isValidPassword
import com.uds.foufoufood.viewmodel.UserViewModel
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.view.DrawerContent
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(navController: NavHostController, userViewModel: UserViewModel) {
    // Extraire les données de l'utilisateur connecté
    val name = userViewModel.user.value?.name ?: ""
    val email = userViewModel.user.value?.email ?: ""
    val role = userViewModel.user.value?.role

    val errorMessage by userViewModel.errorMessage.observeAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController, closeDrawer = {
                    scope.launch { drawerState.close() }
                },
                logout = userViewModel::logout, userViewModel = userViewModel
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize().padding(end = 20.dp, top = 20.dp),
            ) {
                IconButton(
                    onClick = {
                        scope.launch { drawerState.open() }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd) // Placer en haut à droite
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                TitlePage(label = stringResource(id = R.string.my_profile))

                ProfileName(name = name)

                Spacer(modifier = Modifier.height(16.dp))

                if (role != null) {
                    ProfileType(role = role.capitalizeFirstLetter())
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
                        } else {
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
                    onSaveClick = { previousPassword, newPassword ->
                        if (isValidPassword(newPassword) && isValidPassword(previousPassword)) {
                            userViewModel.updatePassword(previousPassword, newPassword)
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
            }

            errorMessage?.let {
                Text(text = "$it", color = Color.Red, modifier = Modifier.padding(16.dp))
            }
        }
    )
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

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.email), // Libellé pour "Email"
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(end = 10.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

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

    Spacer(modifier = Modifier.height(5.dp))

    Column {
        if (!isEditable) {
            // Bouton pour activer l'édition de l'email
            Button(
                onClick = { isEditable = true }, // Passer en mode édition
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange)) // Optionnel : changer la couleur du bouton
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically // Aligner verticalement
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pen_square_icon),
                        contentDescription = "Edit Email",
                        modifier = Modifier.size(30.dp) // Ajuster la taille de l'icône si nécessaire
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'icône et le texte
                    Text(text = "Modifier") // Le texte du bouton
                }
            }
        } else {
            Button(
                onClick = {
                    if (!isError) {
                        isEditable = false // Désactiver le mode édition
                        onSaveClick(email) // Appeler la fonction de sauvegarde
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange_pale)) // Optionnel : changer la couleur du bouton
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically // Aligner verticalement
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = "Save Email",
                        modifier = Modifier.size(30.dp) // Ajuster la taille de l'icône si nécessaire
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'icône et le texte
                    Text(text = "Enregistrer") // Le texte du bouton
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePasswordSection(
    onSaveClick: (String, String) -> Unit
) {
    var isEditable by remember { mutableStateOf(false) }
    var previousPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isNewPasswordError by remember { mutableStateOf(false) }
    var isPreviousPasswordError by remember { mutableStateOf(false) }
    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isPreviousPasswordVisible by remember { mutableStateOf(false) }

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
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Champ de texte pour afficher/modifier le mot de passe
        if (isEditable) {
            TextField(
                label = { Text(stringResource(id = R.string.previous_password)) },
                value = previousPassword,
                onValueChange = {
                    previousPassword = it
                    isPreviousPasswordError = !isValidPassword(it) // Valider le mot de passe
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
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = colorResource(id = R.color.black),
                ),
                visualTransformation = if (isPreviousPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = isPreviousPasswordError,
                supportingText = {
                    if (isPreviousPasswordError) {
                        Text(stringResource(R.string.password_constraint), color = Color.Red)
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        isPreviousPasswordVisible = !isPreviousPasswordVisible
                    }) {
                        Image(
                            painter = painterResource(id = if (isPreviousPasswordVisible) R.drawable.visibility_off else R.drawable.visibility),
                            contentDescription = if (isPreviousPasswordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                enabled = isEditable, // Activer ou désactiver l'édition
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                label = { Text(stringResource(id = R.string.new_password)) },
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    isNewPasswordError = !isValidPassword(it) // Valider le mot de passe
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
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = colorResource(id = R.color.black),
                ),
                visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = isNewPasswordError,
                supportingText = {
                    if (isNewPasswordError) {
                        Text(stringResource(R.string.password_constraint), color = Color.Red)
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                        Image(
                            painter = painterResource(id = if (isNewPasswordVisible) R.drawable.visibility_off else R.drawable.visibility),
                            contentDescription = if (isNewPasswordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                enabled = isEditable, // Activer ou désactiver l'édition
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Text(
                text = stringResource(id = R.string.password_instructions), // Mot de passe masqué
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(15.dp))

    if (!isEditable) {
        // Bouton pour activer l'édition du mot de passe
        Button(
            onClick = { isEditable = true }, // Passer en mode édition
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange)) // Optionnel : changer la couleur du bouton
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically // Aligner verticalement
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pen_square_icon),
                    contentDescription = "Edit Password",
                    modifier = Modifier.size(30.dp) // Ajuster la taille de l'icône si nécessaire
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'icône et le texte
                Text(text = "Modifier") // Le texte du bouton
            }
        }
    } else {
        // Bouton pour sauvegarder le mot de passe modifié
        Button(
            onClick = {
                if (!isNewPasswordError && !isPreviousPasswordError) {
                    isEditable = false // Désactiver le mode édition
                    onSaveClick(previousPassword, newPassword) // Appeler la fonction de sauvegarde
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange_pale)) // Optionnel : changer la couleur du bouton
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically // Aligner verticalement
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.save),
                    contentDescription = "Save Password",
                    modifier = Modifier.size(30.dp) // Ajuster la taille de l'icône si nécessaire
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'icône et le texte
                Text(text = "Enregistrer") // Le texte du bouton
            }
        }
    }
}

fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}