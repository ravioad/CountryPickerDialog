package com.example.customcountrypicker.countryPickerStuff

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.customcountrypicker.R

class BlurView : RelativeLayout {
    constructor(context: Context, color: Int = Color.BLACK) : super(context) {
        init(color)
    }

    private fun init(color: Int) {
        View.inflate(context, R.layout.blur_view_layout, this)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = 80f
        }
        this.setBackgroundColor(color)
        background.alpha = 214
        this.isClickable = true
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {

    }
}