package com.uds.foufoufood.view.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.NetworksButtons
import com.uds.foufoufood.ui.component.PasswordTextField
import com.uds.foufoufood.ui.component.TextFieldWithError
import com.uds.foufoufood.ui.component.TextLink
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.ui.component.ValidateButton
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun RegisterFirstPartScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current

    // Champs d'entrée utilisateur (nom, email, mot de passe)
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observer LiveData registrationInitSuccess avec observeAsState
    val registrationSuccess by userViewModel.registrationInitSuccess.observeAsState()

    // Réagir au changement d'état de registrationSuccess
    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess == true) {
            Toast.makeText(context, "Veuillez vérifier votre boîte de réception, un code de vérification vous a été envoyé", Toast.LENGTH_SHORT).show()
            navController.navigate("verify_code/${email}")
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

        TitlePage(label = stringResource(id = R.string.sign_up))

        Spacer(modifier = Modifier.height(10.dp))

        TextFieldWithError(
            value = name,
            onValueChange = { name = it },
            label = stringResource(id = R.string.full_name),
            errorMessage = "Veuillez entrer votre prénom",
            isValid = { it.isNotEmpty() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextFieldWithError(
            value = email,
            onValueChange = { email = it },
            label = stringResource(id = R.string.email),
            errorMessage = "Veuillez entrer une adresse email valide",
            isValid = { isValidEmail(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(id = R.string.password),
            errorMessage = "Le mot de passe doit contenir au moins 6 caractères",
            isValid = { isValidPassword(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        ValidateButton(label = stringResource(id = R.string.next), onClick = {
            if (isValidName(name) && isValidEmail(email) && isValidPassword(password)) {
                userViewModel.initiateRegistration(name, email, password)
            } else {
                Toast.makeText(context, "Veuillez entrer des informations valides", Toast.LENGTH_SHORT).show()
            }
        })

        Spacer(modifier = Modifier.height(20.dp))

        SignInText{
            navController.navigate(Screen.Register.route)
        }

        Spacer(modifier = Modifier.height(20.dp))

        NetworksButtons(stringResource(id = R.string.sign_up_with), Color.Gray)
    }
}

fun isValidName(name: String): Boolean {
    return name.isNotEmpty()
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun SignInText(onNavigateToLogin: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.already_account),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(10.dp))

        TextLink(label = stringResource(id = R.string.sign_in_underlined), onClick = onNavigateToLogin)
    }
}