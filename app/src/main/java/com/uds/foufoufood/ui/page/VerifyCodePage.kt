package com.uds.foufoufood.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.R
import com.uds.foufoufood.ui.theme.TextLink
import com.uds.foufoufood.ui.theme.TitlePage
import com.uds.foufoufood.ui.theme.ValidateButton

@Composable
fun VerifyCodeScreen(
    onVerifyClick: (String) -> Unit,
    onResendClick: () -> Unit
) {
    var code by remember { mutableStateOf(List(6) { "" }) }

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
        NoCodeReceivedSection(onResendClick = onResendClick)

        // Bouton de validation
        ValidateButton(
            label = stringResource(id = R.string.verify),
            onClick = {
                val enteredCode = code.joinToString("")
                onVerifyClick(enteredCode) // Appelle la fonction de vérification
            }
        )
    }
}

@Composable
fun VerificationCodeInput(code: List<String>, onCodeChanged: (Int, String) -> Unit) {
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
                    onValueChange = { onCodeChanged(i, it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun VerificationCodeEditText(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .size(50.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        textStyle = TextStyle(
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular))
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        maxLines = 1
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

@Preview
@Composable
fun Preview() {
    VerifyCodeScreen(
        onVerifyClick = {},
        onResendClick = {}
    )
}