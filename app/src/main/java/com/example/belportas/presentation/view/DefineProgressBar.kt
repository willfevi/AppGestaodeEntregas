package com.example.belportas.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
    val colorOrange = Color(0xFFFFA500)

    Text(
        text = when (deliveryStatus) {
            DeliveryStatus.PEDIDO_SEPARADO -> "Pedido separado :"
            DeliveryStatus.PEDIDO_EM_TRANSITO -> "Pedido em trânsito :"
            DeliveryStatus.PEDIDO_ENTREGUE -> "Pedido entregue :"
            DeliveryStatus.PEDIDO_EXCLUIDO -> "Pedido excluido."
        },
        style = MaterialTheme.typography.subtitle2
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier
                .widthIn(max = 200.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val progress = when (deliveryStatus) {
                DeliveryStatus.PEDIDO_SEPARADO -> 1
                DeliveryStatus.PEDIDO_EM_TRANSITO -> 2
                DeliveryStatus.PEDIDO_ENTREGUE -> 3
                DeliveryStatus.PEDIDO_EXCLUIDO -> 4
            }

            for (i in 1..3) {
                val boxColor = when {
                    deliveryStatus == DeliveryStatus.PEDIDO_EXCLUIDO -> colorOrange
                    i <= progress -> colorGreen
                    else -> colorGrey
                }

                val imageVector = when {
                    deliveryStatus == DeliveryStatus.PEDIDO_EXCLUIDO -> Icons.Filled.Close
                    i <= progress -> Icons.Filled.Check
                    else -> null
                }

                Box(
                    Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(boxColor),
                    contentAlignment = Alignment.Center
                ) {
                    imageVector?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = "Stage $i ${if (deliveryStatus == DeliveryStatus.PEDIDO_EXCLUIDO) "Excluded" else "Complete"}",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                if (i != 3) {
                    val spacerColor = when {
                        deliveryStatus == DeliveryStatus.PEDIDO_EXCLUIDO -> colorOrange
                        i < progress -> colorGreen
                        else -> colorGrey
                    }

                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .weight(1f)
                            .background(spacerColor)
                    )
                }
            }
        }
    }
}
