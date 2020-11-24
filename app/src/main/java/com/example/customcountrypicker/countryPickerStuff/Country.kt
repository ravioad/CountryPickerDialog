package com.example.customcountrypicker.countryPickerStuff

class Country(private val name: String, private val flag: Int, private val code: String) {

    fun getName(): String {
        return name
    }

    fun getFlag(): Int {
        return flag
    }

    fun getCode(): String {
        return code
    }
}