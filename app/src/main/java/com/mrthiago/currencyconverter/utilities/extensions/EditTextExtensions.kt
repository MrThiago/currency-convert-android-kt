package com.mrthiago.currencyconverter.utilities.extensions

import android.text.TextUtils
import android.widget.EditText

fun EditText.placeCursorToEnd() {
    if(!TextUtils.isEmpty(this.text)){
        this.setSelection(this.text.length)
    }
}