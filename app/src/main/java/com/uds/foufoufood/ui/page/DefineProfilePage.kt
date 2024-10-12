package com.uds.foufoufood.ui.page

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.R
import com.uds.foufoufood.ui.theme.RadioButtonWithLabel
import com.uds.foufoufood.ui.theme.TitlePage
import com.uds.foufoufood.ui.theme.ValidateButton

@Composable
fun DefineProfileScreen(onValidateClick: () -> Unit) {
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

        ProfileChoiceSection()

        ValidateButton(label = stringResource(id = R.string.validate), onClick = onValidateClick)
    }
}

@Composable
fun ProfileChoiceSection() {
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

        var selectedProfile by remember { mutableStateOf("") }

        ProfileRadioButtons(selectedProfile) { profile ->
            selectedProfile = profile
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.select_option),
            fontSize = 16.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular))
        )
    }
}

@Composable
fun ProfileRadioButtons(selectedProfile: String, onProfileSelected: (String) -> Unit) {
    // Précharger les chaînes de caractères à l'extérieur du lambda
    val customerLabel = stringResource(id = R.string.customer)
    val ownerLabel = stringResource(id = R.string.owner_restau)
    val deliveryManLabel = stringResource(id = R.string.delivery_man)

    Column {
        RadioButtonWithLabel(
            label = customerLabel,
            selected = selectedProfile == customerLabel,
            onSelect = { onProfileSelected(customerLabel) }
        )
        RadioButtonWithLabel(
            label = ownerLabel,
            selected = selectedProfile == ownerLabel,
            onSelect = { onProfileSelected(ownerLabel) }
        )
        RadioButtonWithLabel(
            label = deliveryManLabel,
            selected = selectedProfile == deliveryManLabel,
            onSelect = { onProfileSelected(deliveryManLabel) }
        )
    }
}

@Preview
@Composable
fun PreviewDefineProfileScreen() {
    DefineProfileScreen(onValidateClick = {})
}