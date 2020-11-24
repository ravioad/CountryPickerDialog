package com.example.customcountrypicker.countryPickerDialog

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customcountrypicker.R
import com.example.customcountrypicker.countryPickerStuff.BlurView
import com.example.customcountrypicker.countryPickerStuff.Country
import com.example.customcountrypicker.countryPickerStuff.GetCountriesDataClass
import kotlinx.android.synthetic.main.country_picker_dialog_layout.view.*
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil
import java.util.*
import kotlin.collections.ArrayList

class CountryPickerDialog(
    context: Context,
    val blurView: BlurView
) : RelativeLayout(context) {
    private var bottomPos = 0f
    private var centerPos = 0f
    private var animator: ObjectAnimator? = null
    private var sizeLoaded = MutableLiveData<Boolean>()
    private var parentWidth: Int? = null

    private lateinit var data: ArrayList<Country>
    private var countriesDataLoaded = false
    private var isCurrentlyShown = false
    private lateinit var onSizeChangedArray: Array<Int>

    init {
        sizeLoaded.value = false
        initializeView()
        setKeyboardListener()
    }

    private fun setKeyboardListener() {
        KeyboardVisibilityEvent.setEventListener(
            (context as AppCompatActivity),
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    if (isCurrentlyShown) {
                        if (this@CountryPickerDialog.y == bottomPos) {
                            return
                        }
                        if (!isOpen) {
                            animatePickerTo(centerPos, 250)
                        }
                    }
                }
            })
    }

    private fun initializeView() {
        View.inflate(context, R.layout.country_picker_dialog_layout, this)
        this.elevation = 85f

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onSizeChangedArray = arrayOf(w, h, oldw, oldh)
        val parentHeight = (this.parent as View).height
        parentWidth = (this.parent as View).width
        bottomPos = parentHeight.toFloat()
        centerPos = (parentHeight / 2).toFloat() - (this.height / 2)
        if (!isCurrentlyShown) {
            this.visibility = View.GONE
        } else {
            // Will always be at center is already shown
            animatePickerTo(centerPos, 250)
        }
        sizeLoaded.value = true

        // Get data for country picker
        data = GetCountriesDataClass().getData(context)
    }

    fun showDialog() {
        (context as AppCompatActivity).lifecycleScope.launch {
            if (sizeLoaded.value == true) {
                showLoadedView()
            } else {
                sizeLoaded.observe(context as AppCompatActivity, {
                    if (sizeLoaded.value == true) {
                        showLoadedView()
                    }
                })
            }
        }
    }

    private fun setupRecyclerView(list: ArrayList<Country>) {
        countryPickerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CountryPickerRecyclerViewAdapter(context, list, this@CountryPickerDialog)
        }
    }

    private fun showLoadedView() {
        if (!countriesDataLoaded) {
            setupRecyclerView(data)
            countryPickerRecyclerView.scrollToPosition(161)
            countriesDataLoaded = true
        }
        if (countriesDataLoaded) {
            addSearch()
        }
        isCurrentlyShown = true
        blurView.visibility = View.VISIBLE
        this.y = bottomPos
        this.visibility = View.VISIBLE
        animatePickerTo(centerPos, 400)
    }

    private fun animatePickerTo(y: Float, duration: Int) {
        animator = ObjectAnimator.ofFloat(this, "Y", y)
        animator?.duration = duration.toLong()
        animator?.start()
    }

    private fun addSearch() {
        countryPickerSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredList = ArrayList<Country>()
                if (p0.toString() != "") {
                    for (item in data) {
                        val countryName = item.getName()
                        if (countryName.toLowerCase(Locale.ROOT)
                                .contains(p0.toString().toLowerCase(Locale.ROOT))
                        ) {
                            filteredList.add(item)
                        }
                    }
                    setupRecyclerView(filteredList)
                } else {
                    setupRecyclerView(data)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }


    fun closeCountryPicker() {
        if (isCurrentlyShown) {
            isCurrentlyShown = false
            animator = ObjectAnimator.ofFloat(this, "Y", bottomPos)
            animator?.duration = 350
            animator?.start()
            animator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    this@CountryPickerDialog.visibility = View.GONE
                    blurView.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {


                }

            })
        }
    }
}