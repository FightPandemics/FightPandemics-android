package com.fightpandemics.profile.ui.profile

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator


open class BaseFragment  : Fragment(), Validator.ValidationListener {

    lateinit var validator : Validator
    var validationOk : () -> Unit = {}
    var validationError : () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validator = Validator(this)
        validator.setValidationListener(this)
    }

    override fun onValidationSucceeded() {
        validationOk?.invoke()
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        if (errors != null) {
            for (error in errors) {
                val view = error.view
                val message = error.getCollatedErrorMessage(requireContext())

                // Display error messages ;)
                if (view is EditText) {
                    view.error = message
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
            validationError?.invoke()
        }
    }
}