package com.example.belportas.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.belportas.data.DeliveryStatus

@Composable
fun DefineProgressBar(deliveryStatus: DeliveryStatus) {
    val colorGreen = Color(0xFF4CAF50)
    val colorGrey = Color(0xFF8B8A89)

    Text(
        text = when (deliveryStatus) {
            DeliveryStatus.PEDIDO_SEPARADO -> "Pedido separado :"
            DeliveryStatus.PEDIDO_EM_TRANSITO -> "Pedido em trânsito :"
            DeliveryStatus.PEDIDO_ENTREGUE -> "Pedido entregue :"
        },
        style = MaterialTheme.typography.subtitle2
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.CenterStart // Aligning content to start
    ) {
        Row(
            Modifier
                .widthIn(max = 200.dp),
            horizontalArrangement = Arrangement.Start, // Start arrangement from start
            verticalAlignment = Alignment.CenterVertically
        ) {
            val progress = when (deliveryStatus) {
                DeliveryStatus.PEDIDO_SEPARADO -> 1
                DeliveryStatus.PEDIDO_EM_TRANSITO -> 2
                DeliveryStatus.PEDIDO_ENTREGUE -> 3
            }

            for (i in 1..3) {
                if (i <= progress) {
                    Box(
                        Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(colorGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Stage $i Complete",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                } else {
                    Box(
                        Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(colorGrey)
                    )
                }

                if (i != 3) {
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .weight(1f)
                            .background(if (i < progress) colorGreen else colorGrey)
                    )
                }
            }
        }
    }
}
