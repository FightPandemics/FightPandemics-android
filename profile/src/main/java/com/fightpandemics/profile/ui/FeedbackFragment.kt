package com.fightpandemics.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.databinding.FeedbackFragmentBinding
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.feedback_fragment.view.*
import kotlinx.android.synthetic.main.feedback_question1.view.*
import kotlinx.android.synthetic.main.profile_toolbar.*
import timber.log.Timber
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
            feedbackViewModel.displayActiveBorder(binding.feedbackQuestion1.root, hasFocus)
        }

        binding.feedbackQuestion2.root.your_answer_edit_text.setOnFocusChangeListener { view, hasFocus ->
            feedbackViewModel.displayActiveBorder(binding.feedbackQuestion2.root, hasFocus)
        }

    }


}
