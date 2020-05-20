package com.fightpandemics.home.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fightpandemics.R

class RequestsFragment: Fragment(), RequestsContract.View {

    private lateinit var presenter: RequestsContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_requests, container, false)
        setPresenter(RequestsPresenter(this))

//        val textView: TextView = root.findViewById(R.id.section_label)
//        textView.text = "REQUESTS"

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(): RequestsFragment {
            return RequestsFragment()
        }
    }

    override fun setPresenter(presenter: RequestsContract.Presenter) {
        this.presenter = presenter
    }
}