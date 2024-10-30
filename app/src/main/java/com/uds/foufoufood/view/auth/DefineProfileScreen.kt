package com.uds.foufoufood.view.auth


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.uds.foufoufood.navigation.getStartDestination
import com.uds.foufoufood.ui.component.RadioButtonWithLabel
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.ui.component.ValidateButton
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun DefineProfileScreen(
    navController: NavController, userViewModel: UserViewModel, email: String
) {
    val registrationCompleteSuccess by userViewModel.registrationCompleteSuccess.observeAsState()

    var selectedProfile by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(registrationCompleteSuccess) {
        if (registrationCompleteSuccess == true) {
            Toast.makeText(context, "Inscription réussie", Toast.LENGTH_SHORT).show()
            userViewModel.resetStatus()
            val startDestination = getStartDestination(selectedProfile.lowercase(), true)
            navController.navigate(startDestination) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        } else if (registrationCompleteSuccess == false) {
            Toast.makeText(context, "Échec de l'inscription", Toast.LENGTH_SHORT).show()
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
        Spacer(modifier = Modifier.height(25.dp))

        TitlePage(label = stringResource(id = R.string.who_are_you))

        Spacer(modifier = Modifier.height(16.dp))

        ProfileChoiceSection(selectedProfile = selectedProfile,
            onProfileSelected = { selectedProfile = it })

        Spacer(modifier = Modifier.height(16.dp))

        ValidateButton(label = stringResource(id = R.string.validate), onClick = {
            if (selectedProfile.isEmpty()) {
                Toast.makeText(context, "Veuillez choisir un type de profil", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Log.d("DefineProfileScreen", "Email: $email, Profile: $selectedProfile")
                userViewModel.completeRegistration(email, selectedProfile)
            }
        })
    }
}

@Composable
fun ProfileChoiceSection(
    selectedProfile: String, onProfileSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.who_are_you_text),
            fontSize = 16.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular))
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileRadioButtons(
            selectedProfile = selectedProfile, onProfileSelected = onProfileSelected
        )
    }
}

@Composable
fun ProfileRadioButtons(
    selectedProfile: String, onProfileSelected: (String) -> Unit
) {
    val customerLabel = stringResource(id = R.string.customer)
    val ownerLabel = stringResource(id = R.string.owner_restau)
    val deliveryManLabel = stringResource(id = R.string.delivery_man)

    Column {
        RadioButtonWithLabel(label = customerLabel,
            selected = selectedProfile == customerLabel,
            onSelect = { onProfileSelected(customerLabel) })
        RadioButtonWithLabel(label = ownerLabel,
            selected = selectedProfile == ownerLabel,
            onSelect = { onProfileSelected(ownerLabel) })
        RadioButtonWithLabel(label = deliveryManLabel,
            selected = selectedProfile == deliveryManLabel,
            onSelect = { onProfileSelected(deliveryManLabel) })
    }
}