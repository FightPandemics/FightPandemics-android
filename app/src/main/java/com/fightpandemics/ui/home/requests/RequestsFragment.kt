package com.fightpandemics.ui.home.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fightpandemics.R
import kotlinx.android.synthetic.main.fragment_offers.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestsFragment: Fragment() {

    private val viewModel: RequestsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent()
    }

    companion object {
        @JvmStatic
        fun newInstance(): RequestsFragment {
            return RequestsFragment()
        }
    }

    fun setContent() {
        section_label.text = viewModel.setContent()
    }
}