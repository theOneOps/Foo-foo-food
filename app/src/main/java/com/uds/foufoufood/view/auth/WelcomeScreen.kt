package com.uds.foufoufood.view.auth

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.getStartDestination
import com.uds.foufoufood.ui.component.NetworksButtons
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun WelcomeScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    googleSignInClient: GoogleSignInClient,
    auth: FirebaseAuth
) {
    val context = LocalContext.current

    val user by userViewModel.user.observeAsState()
    val emailValidated by userViewModel.emailValidated.observeAsState()
    val registrationComplete by userViewModel.registrationCompleteSuccess.observeAsState()
    val loginSuccess by userViewModel.loginSuccess.observeAsState()
    val errorMessage by userViewModel.errorMessage.observeAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task, auth, context, userViewModel)
    }

    LaunchedEffect(user, loginSuccess) {
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
                navController.navigate("verify_code/${user?.email}")
            } else if (registrationComplete == false) {
                navController.navigate("define_profile/${user?.email}")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.welcome_bg),
                contentScale = ContentScale.Crop
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.White.copy(alpha = 0.5f),
                        Color.Black.copy(alpha = 0.5f),
                        Color.Black.copy(alpha = 0.7f),
                    )
                )
            )

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Logo()
            Spacer(modifier = Modifier.height(24.dp))
            WelcomeTitle()
            Spacer(modifier = Modifier.height(16.dp))
            WelcomeDescription()
            Spacer(modifier = Modifier.height(70.dp))
            NetworksButtons(stringResource(id = R.string.sign_in_with), Color.White) {
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
            Spacer(modifier = Modifier.height(30.dp))
            EmailSignUpButton {
                navController.navigate("register")
            }
            Spacer(modifier = Modifier.height(16.dp))
            SignInLink {
                navController.navigate("login")
            }
        }
    }

    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun Logo() {
    Image(
        painter = painterResource(id = R.drawable.full_logo),
        contentDescription = stringResource(id = R.string.app_name),
        modifier = Modifier.size(233.dp)
    )
}

@Composable
fun WelcomeTitle() {
    Text(
        text = stringResource(id = R.string.welcome),
        fontSize = 40.sp,
        fontFamily = FontFamily(Font(R.font.sofiapro_bold)),
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
fun WelcomeDescription() {
    Text(
        text = stringResource(id = R.string.welcome_text),
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 45.dp)
    )
}

@Composable
fun EmailSignUpButton(onNavigateToRegister: () -> Unit) {
    Button(
        onClick = { onNavigateToRegister() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
            .background(colorResource(id = R.color.orange), shape = RoundedCornerShape(30.dp))
    ) {
        Text(
            text = stringResource(id = R.string.start_email),
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SignInLink(onNavigateToLogin: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.already_account),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
            color = Color.White
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = stringResource(id = R.string.sign_in_underlined),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
            color = Color.White,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onNavigateToLogin() })
    }
}