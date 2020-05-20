package com.fightpandemics.home.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fightpandemics.R
import kotlinx.android.synthetic.main.fragment_all.*

class AllFragment : Fragment(), AllContract.View {

    private lateinit var presenter: AllContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_all, container, false)
        setPresenter(AllPresenter(this))
        return root
    }

    override fun onResume() {
        super.onResume()

        presenter.prepareData()
    }

    companion object {
        @JvmStatic
        fun newInstance(): AllFragment {
            return AllFragment()
        }
    }

    override fun setContent(text: String) {
        section_label.text = text
    }

    override fun setPresenter(presenter: AllContract.Presenter) {
        this.presenter = presenter
    }
}