package com.example.belportas.presentation.view
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.belportas.R

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToResetPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val userValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }

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
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_login),
                contentDescription = "Logo",
                modifier = Modifier
                    .widthInFraction(0.5f)
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = userValue.value,
                onValueChange = { userValue.value = it },
                label = { Text(text = "User") },
                modifier = Modifier
                    .widthInFraction(0.95f)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface,
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.onSurface
                )
            )
            OutlinedTextField(
                value = passwordValue.value,
                onValueChange = { passwordValue.value = it },
                label = { Text(text = "Senha") },
                modifier = Modifier
                    .widthInFraction(0.95f)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface,
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.onSurface
                ),visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(
                onClick = {
                    // Aqui você pode adicionar a lógica para autenticar o usuário
                    // Se a autenticação for bem-sucedida, chame onLoginSuccess()
                    onLoginSuccess()
                },
                modifier = Modifier
                    .widthInFraction(0.8f)
                    .padding(top = 8.dp)
            ) {
                Text(text = "Entrar")
            }
            Button(
                onClick = onNavigateToSignUp,
                modifier = Modifier
                    .widthInFraction(0.8f)
                    .padding(top = 8.dp)
            ) {
                Text(text = "Cadastre-se")
            }
            Text(
                text = "Esqueceu a senha?",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.clickable { onNavigateToResetPassword() }
            )
        }
    }
}