package com.example.belportas.model

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class OpenExternalApps {

    fun openMap(context: android.content.Context,address: String) {
        val encodedAddress = Uri.encode(address)

        val googleMapsIntentUri = Uri.parse("google.navigation:q=$encodedAddress")
        val googleMapsIntent = Intent(Intent.ACTION_VIEW, googleMapsIntentUri)

        val wazeIntentUri = Uri.parse("waze://?q=$encodedAddress")
        val wazeIntent = Intent(Intent.ACTION_VIEW, wazeIntentUri)

        val chooserIntent = Intent.createChooser(googleMapsIntent, "Compartilhar localização via").apply {
            val intentsArray = arrayOf(wazeIntent)
            putExtra(Intent.EXTRA_INITIAL_INTENTS, intentsArray)
        }

        try {
            context.startActivity(chooserIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Erro ao abrir o App!", Toast.LENGTH_SHORT).show()
        }
    }

    fun openPhone(context: android.content.Context, phone: String) {
        try {
            val intentUri = Uri.parse("tel:${Uri.encode(phone)}")
            val phoneIntent = Intent(Intent.ACTION_DIAL, intentUri)
            context.startActivity(phoneIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Aplicativo de telefone não encontrado!", Toast.LENGTH_SHORT).show()
        }
    }
    fun openWpp(context: android.content.Context, phone: String, noteNumber: String, address: String) {
        try {
            val formattedPhone =if (phone.length > 9) {
                val areaCode = phone.substring(0, 2)
                val phoneNumber = phone.substring(2)
                "+55 $areaCode $phoneNumber"
            } else {
                phone
            }

            val message = "Olá, seu pedido $noteNumber está próximo de ser entregue nas próximas horas! 🥳 O endereço de entrega é esse mesmo? $address. Caso não seja, por favor, envie sua localização em tempo real!"
            val encodedMessage = Uri.encode(message)
            val intentUri = Uri.parse("https://api.whatsapp.com/send?phone=${Uri.encode(formattedPhone)}&text=$encodedMessage")
            val whatsappIntent = Intent(Intent.ACTION_VIEW, intentUri)
            context.startActivity(whatsappIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Aplicativo do WhatsApp não encontrado!", Toast.LENGTH_SHORT).show()
        }
    }
    fun openWppIncorrectPhones(context: android.content.Context, phone: String, noteNumber: String, address: String) {
        try {
            val message = "Olá, seu pedido $noteNumber está próximo de ser entregue dentre as próximas horas! 🥳 O endereço de entrega é esse mesmo? $address. Caso não seja, por favor, envie sua localização em tempo real! Qualquer duvida pode me perguntar por aqui também!"
            val encodedMessage = Uri.encode(message)
            val intentUri = Uri.parse("https://api.whatsapp.com/send?phone=${Uri.encode(phone)}&text=$encodedMessage")
            val whatsappIntent = Intent(Intent.ACTION_VIEW, intentUri)
            context.startActivity(whatsappIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Aplicativo do WhatsApp não encontrado!", Toast.LENGTH_SHORT).show()
        }
    }

}