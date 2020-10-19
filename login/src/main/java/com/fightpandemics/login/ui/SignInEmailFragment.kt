package com.fightpandemics.login.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import timber.log.Timber
import javax.inject.Inject


class SignInEmailFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    //private val loginViewModel: LoginViewModel by viewModels<LoginViewModel> { loginViewModelFactory }
    private lateinit var sign_in_email_toolbar: MaterialToolbar
    private lateinit var btn_sign_in: MaterialButton
    private lateinit var tv_join_now_instead: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sign_in_email, container, false)

        tv_join_now_instead = rootView.findViewById(R.id.tv_join_now_instead)
        joinNow()

        sign_in_email_toolbar = rootView.findViewById(R.id.sign_in_email_toolbar)
        sign_in_email_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        btn_sign_in = rootView.findViewById(R.id.btn_sign_in)
        btn_sign_in.setOnClickListener {
            hideKeyboard()
        }

        return rootView
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = requireActivity().currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun joinNow() {
        val joinString = SpannableString(getString(R.string.don_t_have_an_account_join_now_instead))

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {

                view.invalidate()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        joinString.setSpan(clickableSpan, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        joinString.setSpan(UnderlineSpan(), 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tv_join_now_instead.setText(joinString, TextView.BufferType.SPANNABLE)
        tv_join_now_instead.setMovementMethod(LinkMovementMethod.getInstance())
    }

}