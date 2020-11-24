package com.example.customcountrypicker.countryPickerStuff

import android.content.Context
import android.graphics.Color
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

class RecyclerAdapter(
    private val context: Context,
    private val list: ArrayList<Country>,
    private val slideView: SlideView
) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

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
        holder.itemView.setBackgroundColor(
            when (position) {
                currentPosition -> ContextCompat.getColor(context, R.color.black)
                else -> Color.TRANSPARENT
            }
        )
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
            slideView.slideDown()
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