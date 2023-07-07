package com.example.belportas.presentation.view

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DefineProgress( indicator : Boolean ){
    val colorGrey= Color(0xFFCCCBCA)
    if (!indicator){
        val colorOrange= Color(0xFFF7673A)
        Text(text = "Status de Entrega: Em trânsito")
        LinearProgressIndicator(
            progress = 0.6f,
            color = (colorOrange),
            modifier = Modifier
                .height(3.dp)
                .clip(RoundedCornerShape(16.dp)),
            backgroundColor = (colorGrey)
        )
    }else {
        val colorGreen= Color(0xFF4CAF50)
        Text(text = "Status de Entrega: Entregue")
        LinearProgressIndicator(
            progress = 1f,
            color = (colorGreen),
            modifier = Modifier
                .height(3.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }

}