package com.fightpandemics.home.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fightpandemics.R
import kotlinx.android.synthetic.main.fragment_offers.*

class RequestsFragment: Fragment(), RequestsContract.View {

    private lateinit var presenter: RequestsContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_requests, container, false)
        setPresenter(RequestsPresenter(this))
        return root
    }

    override fun onResume() {
        super.onResume()
        presenter.prepareData()
    }

    companion object {
        @JvmStatic
        fun newInstance(): RequestsFragment {
            return RequestsFragment()
        }
    }

    override fun setContent(text: String) {
        section_label.text = text
    }

    override fun setPresenter(presenter: RequestsContract.Presenter) {
        this.presenter = presenter
    }
}