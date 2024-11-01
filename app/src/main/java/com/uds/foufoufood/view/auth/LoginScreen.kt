package com.uds.foufoufood.view.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.navigation.getStartDestination
import com.uds.foufoufood.ui.component.NetworksButtons
import com.uds.foufoufood.ui.component.PasswordTextField
import com.uds.foufoufood.ui.component.TextFieldWithError
import com.uds.foufoufood.ui.component.TextLink
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.ui.component.ValidateButton
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    googleSignInClient: GoogleSignInClient,
    auth: FirebaseAuth
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val user by userViewModel.user.observeAsState()
    val errorMessage by userViewModel.errorMessage.observeAsState()
    val emailValidated by userViewModel.emailValidated.observeAsState()
    val registrationComplete by userViewModel.registrationCompleteSuccess.observeAsState()
    val loginSuccess by userViewModel.loginSuccess.observeAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task, auth, context, userViewModel)
    }

    LaunchedEffect(user, loginSuccess) {
        Log.d("LoginScreen", "loginSuccess: $loginSuccess")
        if (loginSuccess == true) {
            if (emailValidated == true && registrationComplete == true) {
                val startDestination = user?.role?.let { getStartDestination(it, emailValidated!!) }
                if (startDestination != null) {
                    navController.navigate(startDestination) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
                userViewModel.resetStatus()
                Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show()
            } else if (emailValidated == false) {
                navController.navigate("verify_code/$email")
            } else if (registrationComplete == false) {
                navController.navigate("define_profile/$email")
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

        TextFieldWithError(value = email,
            onValueChange = { email = it },
            label = "Email",
            errorMessage = "Veuillez entrer une adresse email valide",
            isValid = { isEmailValid(it) })

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(value = password,
            onValueChange = { password = it },
            label = "Password",
            errorMessage = "Le mot de passe doit contenir au moins 6 caractères",
            isValid = { isValidPassword(it) })

        Spacer(modifier = Modifier.height(20.dp))
        ForgotPasswordText()
        Spacer(modifier = Modifier.height(25.dp))

        ValidateButton(label = stringResource(id = R.string.sign_in), onClick = {
            if (isEmailValid(email) && isValidPassword(password)) {
                userViewModel.login(email, password)
            } else {
                Toast.makeText(
                    context, "Veuillez entrer des informations valides", Toast.LENGTH_SHORT
                ).show()
            }
        })

        Spacer(modifier = Modifier.height(20.dp))

        SignUpText {
            navController.navigate(Screen.Register.route)
        }

        Spacer(modifier = Modifier.height(20.dp))
        // Boutons pour se connecter via des réseaux sociaux (si nécessaire)
        NetworksButtons(
            stringResource(id = R.string.sign_in_with), Color.Gray
        ) { googleSignInLauncher.launch(googleSignInClient.signInIntent) }
    }

    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        userViewModel.resetStatus()
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
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.no_account),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(10.dp))

        TextLink(
            label = stringResource(id = R.string.sign_up_underlined), onClick = onNavigateToRegister
        )
    }
}

fun handleSignInResult(
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
                                userViewModel.loginWithGoogle(token, context)
                            }
                        } else {
                            Log.e(
                                "LoginScreen Google Sign-In handleSignInResult",
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
        Log.e("LoginScreen Google Sign-In handleSignInResult", "Error : ${e.message}")
    }
}
