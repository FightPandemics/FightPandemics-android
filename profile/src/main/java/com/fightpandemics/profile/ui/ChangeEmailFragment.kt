package com.fightpandemics.profile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.profile.R
import com.fightpandemics.core.utils.ViewModelFactory
import javax.inject.Inject


class ChangeEmailFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun newInstance() = ChangeEmailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.email_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}