package com.uds.foufoufood.view.auth

import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.NetworksButtons
import com.uds.foufoufood.ui.component.PasswordTextField
import com.uds.foufoufood.ui.component.TextFieldWithError
import com.uds.foufoufood.ui.component.TextLink
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.ui.component.ValidateButton
import com.uds.foufoufood.viewmodel.UserViewModel
import org.json.JSONObject

@Composable
fun RegisterFirstPartScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    googleSignInClient: GoogleSignInClient,
    auth: FirebaseAuth
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val registrationInitSuccess by userViewModel.registrationInitSuccess.observeAsState()
    val registrationGoogleSuccess by userViewModel.registerGoogleSuccess.observeAsState()
    val registrationCompleteStatus by userViewModel.registrationCompleteStatus.observeAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignUpResult(task, auth, context, userViewModel)
    }

    LaunchedEffect(registrationInitSuccess, registrationGoogleSuccess, registrationCompleteStatus) {
        if (registrationInitSuccess == true) {
            Toast.makeText(
                context,
                "Veuillez vérifier votre boîte de réception, un code de vérification vous a été envoyé",
                Toast.LENGTH_SHORT
            ).show()
            userViewModel.resetStatus()
            navController.navigate("verify_code/${email}")
        } else if (registrationInitSuccess == false) {
            Toast.makeText(
                context,
                "Erreur lors de l'inscription, il se peut que l'adresse email soit déjà utilisée",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (registrationGoogleSuccess == true && registrationCompleteStatus == true) {
            Toast.makeText(
                context, "Inscription réussie avec Google", Toast.LENGTH_SHORT
            ).show()
            userViewModel.resetStatus()
            navController.navigate(Screen.HomeRestaurant.route)
        } else if (registrationGoogleSuccess == false && registrationCompleteStatus == false) {
            Toast.makeText(
                context, "Erreur lors de l'inscription avec Google", Toast.LENGTH_SHORT
            ).show()
        } else if (registrationGoogleSuccess == true && registrationCompleteStatus == false) {
            email = userViewModel.user.value?.email ?: ""
            navController.navigate("define_profile/${email}")
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

        TextFieldWithError(value = name,
            onValueChange = { name = it },
            label = stringResource(id = R.string.full_name),
            errorMessage = "Veuillez entrer votre prénom",
            isValid = { it.isNotEmpty() })

        Spacer(modifier = Modifier.height(10.dp))

        TextFieldWithError(value = email,
            onValueChange = { email = it },
            label = stringResource(id = R.string.email),
            errorMessage = stringResource(id = R.string.enter_valid_email),
            isValid = { isValidEmail(it) })

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(value = password,
            onValueChange = { password = it },
            label = stringResource(id = R.string.password),
            errorMessage = "Le mot de passe doit contenir au moins 6 caractères",
            isValid = { isValidPassword(it) })

        Spacer(modifier = Modifier.height(20.dp))

        ValidateButton(label = stringResource(id = R.string.next), onClick = {
            if (isValidName(name) && isValidEmail(email) && isValidPassword(password)) {
                userViewModel.initiateRegistration(name, email, password)
            } else {
                Toast.makeText(
                    context, "Veuillez entrer des informations valides", Toast.LENGTH_SHORT
                ).show()
            }
        })

        Spacer(modifier = Modifier.height(20.dp))

        SignInText {
            navController.navigate(Screen.Login.route)
        }

        Spacer(modifier = Modifier.height(20.dp))

        NetworksButtons(
            stringResource(id = R.string.sign_up_with), Color.Gray
        ) { googleSignInLauncher.launch(googleSignInClient.signInIntent) }
    }
}

fun handleSignUpResult(
    task: Task<GoogleSignInAccount>,
    auth: FirebaseAuth,
    context: Context,
    userViewModel: UserViewModel
) {
    try {
        val account = task.getResult(ApiException::class.java)
        val idToken = account?.idToken

        if (idToken != null) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { signInTask ->
                if (signInTask.isSuccessful) {
                    auth.currentUser?.getIdToken(false)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val newIdToken = tokenTask.result?.token
                            newIdToken?.let { token ->
                                decodeFirebaseToken(token)
                                userViewModel.registerWithGoogle(token) // Utilisez le token pour l'authentification
                            }
                        } else {
                            Log.e(
                                "RegisterFirstPartScreen Google Sign-In handleSignUpResult",
                                "Error : ${tokenTask.exception?.message}"
                            )
                        }
                    }
                } else {
                    Toast.makeText(
                        context, "Échec de l'authentification Google", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(context, "ID Token introuvable", Toast.LENGTH_SHORT).show()
        }
    } catch (e: ApiException) {
        Log.e("RegisterFirstPartScreen Google Sign-In handleSignUpResult", "Erreur : ${e.message}")
    }
}

fun decodeFirebaseToken(firebaseToken: String?) {
    if (firebaseToken == null) {
        return
    }

    try {
        val parts = firebaseToken.split(".")
        if (parts.size < 2) {
            Log.e(
                "RegisterFirstPartScreen decodeFirebaseToken",
                "The token does not have the required parts."
            )
            return
        }

        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))

        val jsonPayload = JSONObject(payload)
        val aud = jsonPayload.optString("aud")
        val iss = jsonPayload.optString("iss")
        val sub = jsonPayload.optString("sub")
        val exp = jsonPayload.optLong("exp")

        Log.d("FirebaseTokenClaims", "aud: $aud, iss: $iss, sub: $sub, exp: $exp")
    } catch (e: Exception) {
        Log.e("decodeFirebaseToken decodeFirebaseToken", "Error : ${e.message}")
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
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.already_account),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(10.dp))

        TextLink(
            label = stringResource(id = R.string.sign_in_underlined), onClick = onNavigateToLogin
        )
    }
}