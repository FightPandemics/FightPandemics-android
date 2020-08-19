package com.fightpandemics.inbox.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.inbox.R

class InboxFragment : Fragment() {

    companion object {
        fun newInstance() = InboxFragment()
    }

    private lateinit var viewModel: InboxViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inbox_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InboxViewModel::class.java)
        // TODO: Use the ViewModel
    }

}