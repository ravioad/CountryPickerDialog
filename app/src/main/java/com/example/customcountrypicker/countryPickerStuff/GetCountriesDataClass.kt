package com.example.customcountrypicker.countryPickerStuff

import android.content.Context
import org.json.JSONArray
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class GetCountriesDataClass {

    private fun getJsonDataFromAsset(context: Context): String? {
        val jsonString: String
        try {
            jsonString =
                context.assets.open("countryCodes.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getData(context: Context): ArrayList<Country> {
        val list = ArrayList<Country>()
        val jsonString = getJsonDataFromAsset(context)
        val jsonArray = JSONArray(jsonString)
        for (index in 0 until jsonArray.length()) {
            val countryname = jsonArray.getJSONObject(index).getString("name")
            val dial_code = jsonArray.getJSONObject(index).getString("dial_code")
            val code = jsonArray.getJSONObject(index).getString("code").toLowerCase(Locale.getDefault())
            val drawableId = context.resources.getIdentifier(code, "drawable", context.packageName)
            list.add(Country(countryname, drawableId, dial_code))
        }
        return list
    }
}