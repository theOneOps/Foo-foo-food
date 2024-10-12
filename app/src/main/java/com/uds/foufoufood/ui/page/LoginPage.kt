package com.uds.foufoufood.ui.page

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
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
fun LoginScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginClick: () -> Unit
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

        TitlePage(label = stringResource(id = R.string.sign_in))

        Spacer(modifier = Modifier.height(10.dp))

        TextFieldWithError(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            errorMessage = "Veuillez entrer une adresse email valide",
            isValid = { isValidEmail(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            errorMessage = "Le mot de passe doit contenir au moins 6 caractères",
            isValid = { isValidPassword(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))
        ForgotPasswordText()
        Spacer(modifier = Modifier.height(25.dp))
        ValidateButton(label = stringResource(id = R.string.sign_in), onClick = onLoginClick)
        Spacer(modifier = Modifier.height(20.dp))
        SignUpText(onNavigateToRegister = onNavigateToRegister)
        Spacer(modifier = Modifier.height(20.dp))
        NetworksButtons(stringResource(id = R.string.sign_in_with), Color.Gray)
    }
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

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        email = "",
        onEmailChange = {},
        password = "",
        onPasswordChange = {},
        onNavigateToRegister = {},
        onLoginClick = {}
    )
}