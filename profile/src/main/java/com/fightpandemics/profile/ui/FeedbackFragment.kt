package com.fightpandemics.profile.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.databinding.FeedbackFragmentBinding
import com.fightpandemics.utils.ViewModelFactory
import javax.inject.Inject

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

        binding.feedbackQuestion1.yourAnswerEditText1.setOnFocusChangeListener { _, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion1.verticalAccent1, hasFocus)
            displayFilledEditText(binding.feedbackQuestion1.yourAnswerEditText1, hasFocus)
        }

        binding.feedbackQuestion2.yourAnswerEditText2.setOnFocusChangeListener { _, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion2.verticalAccent2, hasFocus)
            displayFilledEditText(binding.feedbackQuestion2.yourAnswerEditText2, hasFocus)
        }

        binding.feedbackQuestion3.yourAnswerEditText3.setOnFocusChangeListener { _, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion3.verticalAccent3, hasFocus)
            displayFilledEditText(binding.feedbackQuestion3.yourAnswerEditText3, hasFocus)
        }

        binding.feedbackQuestion4.yourAnswerEditText4.setOnFocusChangeListener { _, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion4.verticalAccent4, hasFocus)
            displayFilledEditText(binding.feedbackQuestion4.yourAnswerEditText4, hasFocus)
        }
    }

    fun displayActiveBorder(verticalAccent: FrameLayout, hasFocus: Boolean) {
        if (hasFocus) {
            verticalAccent.visibility = View.VISIBLE
        } else {
            verticalAccent.visibility = View.GONE
        }
    }

    fun displayFilledEditText(editText: EditText, hasFocus: Boolean) {
        // Only when the text is empty, the view should not be highlighted
        val answer = editText.text.toString()
        if (answer == "" && !hasFocus) {
            editText.backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.empty_edittext_color, null))
        } else {
            editText.backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.filled_edittext_color, null))
        }
    }
}
