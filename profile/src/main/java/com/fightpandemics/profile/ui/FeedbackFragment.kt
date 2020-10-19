package com.fightpandemics.profile.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.databinding.FeedbackFragmentBinding
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.card.MaterialCardView
import javax.inject.Inject
//import com.fightpandemics.profile.databinding

class FeedbackFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var binding: FeedbackFragmentBinding
    lateinit var feedbackViewModel: FeedbackViewModel

    companion object {
        fun newInstance() = FeedbackFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FeedbackFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the viewmodel
        feedbackViewModel = ViewModelProvider(this).get(FeedbackViewModel::class.java)

        binding.feedbackQuestion1.yourAnswerEditText1.setOnFocusChangeListener { view, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion1.verticalAccent1, hasFocus)
            displayFilledEditText(binding.feedbackQuestion1.yourAnswerEditText1, hasFocus)
        }

        binding.feedbackQuestion2.yourAnswerEditText2.setOnFocusChangeListener { view, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion2.verticalAccent2, hasFocus)
            displayFilledEditText(binding.feedbackQuestion2.yourAnswerEditText2, hasFocus)
        }

        binding.feedbackQuestion3.yourAnswerEditText3.setOnFocusChangeListener { view, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion3.verticalAccent3, hasFocus)
            displayFilledEditText(binding.feedbackQuestion3.yourAnswerEditText3, hasFocus)
        }

        binding.feedbackQuestion4.yourAnswerEditText4.setOnFocusChangeListener { view, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion4.verticalAccent4, hasFocus)
            displayFilledEditText(binding.feedbackQuestion4.yourAnswerEditText4, hasFocus)
        }

    }

    fun displayActiveBorder(verticalAccent: FrameLayout, hasFocus: Boolean){
        if (hasFocus){
            verticalAccent.visibility = View.VISIBLE
        }else{
            verticalAccent.visibility = View.GONE
        }
    }

    fun displayFilledEditText(editText: EditText, hasFocus: Boolean){
        /*
        not text & focus = highlight
        text & focus = highlight
        text & no focus = highlight
        not text & no focus = nothing
         */
        val answer = editText.text.toString()
        if (answer == "" && !hasFocus){
            editText.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.empty_edittext_color) ))
        }else{
            editText.setBackgroundTintList(ColorStateList.valueOf( resources.getColor(R.color.filled_edittext_color)))
        }
    }


}
