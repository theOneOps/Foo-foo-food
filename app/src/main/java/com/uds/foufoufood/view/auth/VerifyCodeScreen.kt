package com.uds.foufoufood.view.auth

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.TextLink
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.ui.component.ValidateButton
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun VerifyCodeScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    email: String
) {
    var code by remember { mutableStateOf(List(6) { "" }) }

    val context = LocalContext.current

    val codeVerificationSuccess by userViewModel.codeVerificationSuccess.observeAsState()
    Log.d("VerifyCodeScreen", "Code verification success: $codeVerificationSuccess")
    val user by userViewModel.user.observeAsState()
    userViewModel.getUser(email)
    Log.d("VerifyCodeScreen", "User: $user")

    LaunchedEffect(codeVerificationSuccess) {
        if (codeVerificationSuccess == true) {
            Log.d("VerifyCodeScreen", "Code verification success")
            Log.d("VerifyCodeScreen", "Registration complete: ${user?.registrationComplete}")
            if (user?.registrationComplete == true) {
                Toast.makeText(context, R.string.email_update_success, Toast.LENGTH_SHORT).show()
                userViewModel.resetCodeVerificationStatus()
                navController.navigate(Screen.Profile.route)
            }
            else if (user?.registrationComplete == false) {
                Toast.makeText(context, "Code vérifié avec succès", Toast.LENGTH_SHORT).show()
                userViewModel.resetCodeVerificationStatus()
                navController.navigate("define_profile/${email}")
            }
            else {
                Log.d("VerifyCodeScreen", "${user?.registrationComplete}")
            }
        } else if (codeVerificationSuccess == false) {
            Toast.makeText(context, "Le code est incorrect", Toast.LENGTH_SHORT).show()
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
        TitlePage(label = stringResource(id = R.string.verification_code))

        // Champ de saisie du code
        VerificationCodeInput(
            code = code,
            onCodeChanged = { index, value ->
                if (value.length <= 1) {
                    code = code.toMutableList().also { it[index] = value }
                }
            }
        )

        // Section "Pas reçu de code"
        NoCodeReceivedSection {
            userViewModel.resendVerificationCode(email)
        }

        // Bouton de validation
        ValidateButton(
            label = stringResource(id = R.string.verify),
            onClick = {
                val enteredCode = code.joinToString("")
                if (enteredCode.length == 6) {
                    userViewModel.verifyCode(email, enteredCode)
                } else {
                    Toast.makeText(context, "Veuillez entrer le code complet", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }
}

@Composable
fun VerificationCodeInput(code: List<String>, onCodeChanged: (Int, String) -> Unit) {
    val focusRequesters = List(6) { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.verification_text),
            fontSize = 16.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
            modifier = Modifier.padding(10.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0..5) {
                VerificationCodeEditText(
                    value = code[i],
                    onValueChange = { value ->
                        if (value.length <= 1) {
                            onCodeChanged(i, value)
                            if (value.isNotEmpty() && i < 5) {
                                // Passer au champ suivant
                                focusRequesters[i + 1].requestFocus()
                            }
                        }
                    },
                    onBackspace = {
                        if (code[i].isEmpty() && i > 0) {
                            // Revenir au champ précédent si on appuie sur backspace et le champ est vide
                            onCodeChanged(i - 1, "") // Vider le champ précédent
                            focusRequesters[i - 1].requestFocus()
                        }
                    },
                    focusRequester = focusRequesters[i]
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun VerificationCodeEditText(
    value: String,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    focusRequester: FocusRequester
) {
    TextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= 1) {
                onValueChange(newValue)
            }
        },
        modifier = Modifier
            .width(45.dp)
            .focusRequester(focusRequester)
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Backspace && keyEvent.type == KeyEventType.KeyDown && value.isEmpty()) {
                    onBackspace() // Appeler onBackspace() quand le champ est vide
                    true
                } else {
                    false
                }
            },
        singleLine = true,
        maxLines = 1,
        textStyle = TextStyle(
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun NoCodeReceivedSection(onResendClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(id = R.string.no_receive),
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.sofiapro_medium))
        )
        Spacer(modifier = Modifier.width(10.dp))

        TextLink(
            label = stringResource(id = R.string.resend),
            onClick = onResendClick // Appelle la fonction de renvoi du code
        )
    }
}
