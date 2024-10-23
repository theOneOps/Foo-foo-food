package com.uds.foufoufood.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.R


@Composable
fun TitlePage(label: String) {
    Text(
        text = label,
        fontSize = 37.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily(Font(R.font.sofiapro_semibold)),
        modifier = Modifier.fillMaxWidth(),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        color = Color.Black,
    )

    Spacer(modifier = Modifier.height(50.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String,
    isValid: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            isError = !isValid(it)
        },
        label = {
            Text(
                label,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.sofiapro_regular))
            )
        },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(errorMessage, color = Color.Red)
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = colorResource(id = R.color.black),
            unfocusedTextColor = Color.Gray,
            focusedIndicatorColor = colorResource(id = R.color.orange),
            unfocusedIndicatorColor = Color.Gray,
            errorIndicatorColor = Color.Red,
            cursorColor = colorResource(id = R.color.orange),
            containerColor = colorResource(id = R.color.white),
            errorContainerColor = colorResource(id = R.color.white_grey),
        ),
        modifier = modifier.fillMaxWidth(),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String,
    isValid: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            isError = !isValid(it)
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = colorResource(id = R.color.black),
            unfocusedTextColor = Color.Gray,
            focusedIndicatorColor = colorResource(id = R.color.orange),
            unfocusedIndicatorColor = Color.Gray,
            errorIndicatorColor = Color.Red,
            cursorColor = colorResource(id = R.color.orange),
            containerColor = colorResource(id = R.color.white),
            errorContainerColor = colorResource(id = R.color.white_grey),
        ),
        label = {
            Text(
                label,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.sofiapro_regular))
            )
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(errorMessage, color = Color.Red)
            }
        },
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Image(
                    painter = painterResource(id = if (isPasswordVisible) R.drawable.visibility_off else R.drawable.visibility),
                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun ValidateButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .height(60.dp)
            .padding(horizontal = 50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange)),
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.sofiapro_semibold))
        )
    }
}

@Composable
fun FacebookButton() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(30.dp))
            .size(140.dp, 54.dp)
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.facebook_icon),
            contentDescription = stringResource(id = R.string.facebook),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(id = R.string.facebook), fontSize = 14.sp)
    }
}

@Composable
fun GoogleButton() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(30.dp))
            .size(140.dp, 54.dp)
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_icon),
            contentDescription = stringResource(id = R.string.google),
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(id = R.string.google), fontSize = 14.sp)
    }
}

@Composable
fun NetworksButtons(type: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 60.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = type,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                color = color
            )
            Spacer(modifier = Modifier.width(8.dp))
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = color
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            FacebookButton()
            Spacer(modifier = Modifier.width(35.dp))
            GoogleButton()
        }
    }
}


@Composable
fun RadioButtonWithLabel(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFFFA500) // Couleur orange
            )
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.sofiapro_regular))
        )
    }
}

@Composable
fun TextLink(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
        textDecoration = TextDecoration.Underline,
        color = colorResource(id = R.color.orange),
        modifier = Modifier.clickable { onClick() }
    )
}
