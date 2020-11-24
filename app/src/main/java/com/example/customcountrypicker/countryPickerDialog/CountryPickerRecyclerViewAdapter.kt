package com.example.customcountrypicker.countryPickerDialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.customcountrypicker.R
import com.example.customcountrypicker.countryPickerStuff.Country
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil

class CountryPickerRecyclerViewAdapter(
    private val context: Context,
    private val list: ArrayList<Country>,
    private val countryPicker: CountryPickerDialog
) : RecyclerView.Adapter<CountryPickerRecyclerViewAdapter.MyViewHolder>() {

    var currentPosition: Int = 161
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(context, inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val country: Country = list[position]
        val countryCodeTextView =
            (context as AppCompatActivity).findViewById<TextView>(R.id.textView)
        holder.itemView.background = when (position) {
            currentPosition -> ContextCompat.getDrawable(
                context,
                R.drawable.country_picker_item_selected
            )
            else -> null
        }

        holder.name?.setTextColor(
            when (position) {
                currentPosition -> Color.WHITE
                else -> Color.parseColor("#808080")
            }
        )
        holder.bind(country)
        holder.itemView.setOnClickListener {
            notifyItemChanged(currentPosition)
            currentPosition = position
            notifyItemChanged(currentPosition)
            countryCodeTextView.text = "${country.getCode()} "
            if (KeyboardVisibilityEvent.isKeyboardVisible(context as Activity)) {
                UIUtil.hideKeyboard(context as Activity)
            }
            context.runOnUiThread {
                Handler(Looper.getMainLooper()).postDelayed({
                    countryPicker.closeCountryPicker()
                }, 300)
            }
        }

    }

    inner class MyViewHolder(
        private val context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.list_item, parent, false)
        ) {

        var name: TextView? = null
        var countryFlag: ImageView? = null

        init {
            name = itemView.findViewById(R.id.country_name)
            countryFlag = itemView.findViewById(R.id.flagView)
        }

        fun bind(country: Country) {
            name?.text = country.getName()
            countryFlag?.let {
                Glide.with(context).load(country.getFlag())
                    .apply(RequestOptions.circleCropTransform()).into(it)
            }
        }
    }
}