package com.example.belportas.presentation.view
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belportas.data.Task
import com.example.belportas.model.ConfirmImage
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DoneTaskCard(
    task: Task
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateString = task.date?.let { dateFormat.format(it) }
    val hoursFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val hoursString = task.date?.let { hoursFormat.format(it) }
    val confirmImage= ConfirmImage()
    val context = LocalContext.current
    val imageBitmap = remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.25.dp,Color.Gray),
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .shadow(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.List,
                        contentDescription = "Nota",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "Número da Nota: ${task.noteNumber}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "        id:${task.id} ",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = task.clientName,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth(0.8f))
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info entregra",
                    modifier = Modifier
                        .clickable {
                            val loadedImage = confirmImage.loadImageFromInternalStorage(context,"IMG_${task.id}.jpg")
                            imageBitmap.value = loadedImage
                            showDialog.value = true
                        }
                )
            }
            Text(
                text ="    Tel:    ${task.phoneClient}",
                fontWeight=FontWeight.Light,
                style =MaterialTheme.typography.subtitle1
            )
            Text(
                text ="    Data da entrega: $dateString",
                fontWeight=FontWeight.Light,
                style =MaterialTheme.typography.subtitle1
            )
            Text(
                text ="    Hora da entrega: $hoursString ",
                fontWeight=FontWeight.Light,
                style =MaterialTheme.typography.subtitle1
            )
            val indicator = task.deliveryStatus
            DefineProgressBar(indicator)

        }

    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            title = { Text(text = "Imagem da entrega no horário $hoursString e dia $dateString:") },
            text = {
                imageBitmap.value?.asImageBitmap()?.let { img ->
                    Image(
                        bitmap = img,
                        contentDescription = "Imagem Carregada",
                        modifier = Modifier.size(200.dp).clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text("Fechar")
                }
            }
        )
    }
}
