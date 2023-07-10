package com.example.belportas.presentation.view
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
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
                    Box(modifier =Modifier.fillMaxWidth(0.55f)){ Image(
                        painter = painterResource(id = R.drawable.icon_belportas_topbar),
                        contentDescription = "App top bar logo "
                    )}
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
                    .fillMaxWidth(0.5f)
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            CustomOutlinedTextField(userValue, "Usuário:", KeyboardType.Text)
            CustomOutlinedTextField(passwordValue, "Senha:", KeyboardType.Password)
            Button(
                onClick = {
                    onLoginSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 8.dp)
            ) {
                Text(text = "Entrar")
            }
            Button(
                onClick = onNavigateToSignUp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
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
