package com.uds.foufoufood.ui.page

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.auth.isValidEmail
import com.uds.foufoufood.activities.auth.isValidPassword
import com.uds.foufoufood.ui.theme.NetworksButtons
import com.uds.foufoufood.ui.theme.PasswordTextField
import com.uds.foufoufood.ui.theme.TextFieldWithError
import com.uds.foufoufood.ui.theme.TextLink
import com.uds.foufoufood.ui.theme.TitlePage
import com.uds.foufoufood.ui.theme.ValidateButton

@Composable
fun RegisterFirstPartScreen(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
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
            onValueChange = onNameChange,
            label = stringResource(id = R.string.full_name),
            errorMessage = "Veuillez entrer votre prénom",
            isValid = { it.isNotEmpty() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextFieldWithError(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(id = R.string.email),
            errorMessage = "Veuillez entrer une adresse email valide",
            isValid = { isValidEmail(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(id = R.string.password),
            errorMessage = "Le mot de passe doit contenir au moins 6 caractères",
            isValid = { isValidPassword(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        ValidateButton(label = stringResource(id = R.string.next), onClick = onRegisterClick)

        Spacer(modifier = Modifier.height(20.dp))

        SignInText(onNavigateToLogin = onNavigateToLogin)

        Spacer(modifier = Modifier.height(20.dp))

        NetworksButtons(stringResource(id = R.string.sign_up_with), Color.Gray)
    }
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
