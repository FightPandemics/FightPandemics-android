package com.fightpandemics.home.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fightpandemics.R
import kotlinx.android.synthetic.main.fragment_offers.*

class OffersFragment : Fragment(), OffersContract.View {

    private lateinit var presenter: OffersContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_offers, container, false)
        setPresenter(OffersPresenter(this))
        return root
    }

    override fun onResume() {
        super.onResume()
        presenter.prepareData()
    }

    companion object {
        @JvmStatic
        fun newInstance(): OffersFragment {
            return OffersFragment()
        }
    }

    override fun setContent(text: String) {
        section_label.text = text
    }

    override fun setPresenter(presenter: OffersContract.Presenter) {
        this.presenter = presenter
    }
}
