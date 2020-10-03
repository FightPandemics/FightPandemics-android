package com.fightpandemics.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout


/**
 * This extension supports EditText validation such as emails, maximum and minimum length
 * */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString().trim())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

/**
 * You can validate the EditText like
 *
 * et_email.validate("Valid email address required"){ s -> s.isValidEmail() }
 *
 * et_email.validate("Minimum length is 6"){ s-> s.length>=6 }
 *
 * */
fun EditText.validate(message: String, textInputLayout : TextInputLayout, checkLayoutEditText : ICheckLayoutEditText, validator: (String) -> Boolean) : Boolean{
    this.afterTextChanged {
        if(it.isNotEmpty()){
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = if (validator(it)) null else message
        }else{
            textInputLayout.isErrorEnabled = false
        }
        checkLayoutEditText.checkLayout()
    }
    return validator(this.getString())
}



/**
 * @return String value of the EditTextView
 * */
fun EditText.getString(): String{
    return this.text.toString()
}

/**
 * @return Trimmed String value of the EditTextView
 * */
fun EditText.getStringTrim(): String{
    return this.getString().trim()
}

interface ICheckLayoutEditText{
    fun checkLayout()
}

