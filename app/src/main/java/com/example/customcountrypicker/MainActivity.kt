package com.example.customcountrypicker

import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.customcountrypicker.countryPickerDialog.CountryPickerDialog
import com.example.customcountrypicker.countryPickerStuff.BlurView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    var pickerDialog: CountryPickerDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addCountryPicker()

        button.setOnClickListener {
            pickerDialog?.showDialog()
        }
    }

//    private fun openCountryPicker() {
//        // Hide Keyboard before opening Country Picker
//        val view = this.currentFocus
//        view?.let {
//            val imm =
//                this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            imm?.hideSoftInputFromWindow(it.windowToken, 0)
//
//        }
//        Handler(Looper.getMainLooper()).postDelayed({
//            pickerDialog?.slideUp()
//        }, 300)
//
//    }

    private fun addCountryPicker() {
        val blurView = BlurView(this, ContextCompat.getColor(this, R.color.colorPrimary))
        rootLayout.addView(blurView)
        blurView.visibility = View.GONE
        rootLayout.post {
            pickerDialog = CountryPickerDialog(this@MainActivity, blurView)
            rootLayout.addView(pickerDialog)
        }
    }

//    private fun loadDataToCountryPicker() {
//        val data: ArrayList<Country> = GetCountriesDataClass().getData(this)
//        mainAppAuth_countriesRecyclerView.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity)
//            adapter = RecyclerAdapter(this@MainActivity, data, pickerDialog!!)
//        }
//        textView.text = "+92 "
//        mainAppAuth_countriesRecyclerView.scrollToPosition(161)
//    }

}