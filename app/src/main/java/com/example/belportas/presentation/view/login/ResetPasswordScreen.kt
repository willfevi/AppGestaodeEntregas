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
import com.example.belportas.presentation.view.CustomOutlinedTextField


@Composable
fun ResetPasswordScreen(
    onNavigateBack: () -> Unit
) {
    val emailValue = remember { mutableStateOf("") }

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
            Text(
                text = "Digite seu email:",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            CustomOutlinedTextField(emailValue, "E-mail", KeyboardType.Email)
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp)
            ) {
                Text(text = "Receber email")
            }
        }
    }
}
