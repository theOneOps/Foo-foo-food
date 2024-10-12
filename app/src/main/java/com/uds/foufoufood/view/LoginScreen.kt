package com.uds.foufoufood.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.theme.NetworksButtons
import com.uds.foufoufood.ui.theme.PasswordTextField
import com.uds.foufoufood.ui.theme.TextFieldWithError
import com.uds.foufoufood.ui.theme.TextLink
import com.uds.foufoufood.ui.theme.TitlePage
import com.uds.foufoufood.ui.theme.ValidateButton
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val user by userViewModel.user.observeAsState()
    val errorMessage by userViewModel.errorMessage.observeAsState()

    LaunchedEffect(user) {
        user?.let {
            Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show()
            // Naviguer vers l'écran d'accueil une fois connecté
            if (user!!.role == "admin") {
                navController.navigate(Screen.AdminHome.route)
            } else {
                navController.navigate(Screen.Home.route)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        TitlePage(label = stringResource(id = R.string.sign_in))

        Spacer(modifier = Modifier.height(10.dp))

        TextFieldWithError(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            errorMessage = "Veuillez entrer une adresse email valide",
            isValid = { isEmailValid(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            errorMessage = "Le mot de passe doit contenir au moins 6 caractères",
            isValid = { isValidPassword(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))
        ForgotPasswordText()
        Spacer(modifier = Modifier.height(25.dp))

        // Bouton de validation pour la connexion
        ValidateButton(label = stringResource(id = R.string.sign_in), onClick = {
            if (isEmailValid(email) && isValidPassword(password)) {
                userViewModel.login(email, password)
            } else {
                Toast.makeText(context, "Veuillez entrer des informations valides", Toast.LENGTH_SHORT).show()
            }
        })

        Spacer(modifier = Modifier.height(20.dp))

        SignUpText {
            navController.navigate(Screen.Register.route)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Boutons pour se connecter via des réseaux sociaux (si nécessaire)
        NetworksButtons(stringResource(id = R.string.sign_in_with), Color.Gray)
    }

    // Afficher les erreurs de connexion si elles existent
    errorMessage?.let {
        Text(text = "Erreur: $it", color = Color.Red, modifier = Modifier.padding(16.dp))
    }
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 6
}

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun ForgotPasswordText() {
    Text(
        text = stringResource(id = R.string.forgot_password),
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
        color = colorResource(id = R.color.orange),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SignUpText(onNavigateToRegister: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.no_account),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(10.dp))

        TextLink(label = stringResource(id = R.string.sign_up_underlined), onClick = onNavigateToRegister)
    }
}
