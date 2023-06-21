package com.example.belportas.presentation.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.belportas.R

@Composable
fun ResetPasswordScreen(onNavigateToLogin: () -> Unit) {
    val emailValue = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val text = buildAnnotatedString {
                        val customGreen = Color(0xFF4CAF50)
                        withStyle(style = SpanStyle(color = Color.White)) {
                            append("App Bel")
                        }
                        withStyle(style = SpanStyle(color = customGreen)) {
                            append("portas")
                        }
                    }
                    Text(text = text)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Digite seu email:",
                style = MaterialTheme.typography.h6, // Diminuindo o tamanho da fonte
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = emailValue.value,
                onValueChange = { emailValue.value = it },
                label = { Text(text = "E-mail") },
                modifier = Modifier
                    .widthInFraction(0.95f)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface,
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.onSurface
                ),  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Button(
                onClick = { /* Handle reset password */ },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp)
            ) {
                Text(text = "Receber email")
            }
        }
    }
}
