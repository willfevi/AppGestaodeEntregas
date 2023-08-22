package com.example.belportas.model

class ValidatingInputsFromTheAddTaskScreen {
    fun isValidPhoneNumber(value: String): Boolean {
        val regex = "^\\([0-9]{2}\\)[0-9]{4,5}-[0-9]{4}$".toRegex()
        return value.matches(regex)
    }
    fun isValidUF(value: String): Boolean {
        val validUFs = setOf("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO")
        return validUFs.contains(value) && value.length <= 2
    }
    fun isValidCEP(value: String): Boolean {
        val regex = "^[0-9]{2}\\.[0-9]{3}-[0-9]{3}$".toRegex()
        return value.matches(regex)
    }
    fun isValidValue(value: String): Boolean {
        val valueDouble = value.toDoubleOrNull()
        return valueDouble != null && valueDouble > 0
    }
    fun isValidNoteNumber(value: String): Boolean {
        val regex = "^[0-9]{1,6}$".toRegex()
        return value.matches(regex)
    }
}