package com.fightpandemics.profile.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.databinding.FeedbackFragmentBinding
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.feedback_question1.view.*
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

        binding.feedbackQuestion1.root.your_answer_edit_text.setOnFocusChangeListener { view, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion1.root, hasFocus)
            displayFilledEditText(binding.feedbackQuestion1.root.your_answer_edit_text, hasFocus)
        }

        binding.feedbackQuestion2.root.your_answer_edit_text.setOnFocusChangeListener { view, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion2.root, hasFocus)
            displayFilledEditText(binding.feedbackQuestion2.root.your_answer_edit_text, hasFocus)
        }

        binding.feedbackQuestion3.root.your_answer_edit_text.setOnFocusChangeListener { view, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion3.root, hasFocus)
            displayFilledEditText(binding.feedbackQuestion3.root.your_answer_edit_text, hasFocus)
        }

        binding.feedbackQuestion4.root.your_answer_edit_text.setOnFocusChangeListener { view, hasFocus ->
            displayActiveBorder(binding.feedbackQuestion4.root, hasFocus)
            displayFilledEditText(binding.feedbackQuestion4.root.your_answer_edit_text, hasFocus)
        }

    }

    fun displayActiveBorder(materialCard: MaterialCardView, hasFocus: Boolean){
        if (hasFocus){
            materialCard.vertical_accent.visibility = View.VISIBLE
        }else{
            materialCard.vertical_accent.visibility = View.GONE
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
