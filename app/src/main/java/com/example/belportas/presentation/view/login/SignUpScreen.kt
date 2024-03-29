package com.example.belportas.presentation.view.login
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import com.example.belportas.presentation.view.CustomOutlinedTextField

@Composable
fun SingUpScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val nameValue = remember { mutableStateOf("") }
    val codAdminValue = remember { mutableStateOf("") }
    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier =Modifier.fillMaxWidth(0.50f)){ Image(
                        painter = painterResource(id = R.drawable.icon_belportas_topbar),
                        contentDescription = "App top bar logo "
                    )}
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
            Image(
                painter = painterResource(id = R.drawable.ic_cadastro),
                contentDescription = "Cadastro",
                modifier = Modifier.fillMaxWidth(0.5f)

            )
            CustomOutlinedTextField(nameValue, "Nome:", KeyboardType.Text)
            CustomOutlinedTextField(codAdminValue, "Código:", KeyboardType.Text)
            CustomOutlinedTextField(emailValue, "E-mail:", KeyboardType.Email)
            CustomOutlinedTextField(passwordValue,"Senha:", KeyboardType.Text)
            CustomOutlinedTextField(confirmPasswordValue, "Confirme a senha:", KeyboardType.Text)
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp)
            ) {
                Text(text = "Cadastrar")
            }
        }
    }
}
