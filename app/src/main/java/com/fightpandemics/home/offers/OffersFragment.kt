package com.fightpandemics.home.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.R
import com.fightpandemics.ui.base.BaseFragment
import com.fightpandemics.di.component.DaggerOffersFragmentComponent
import com.fightpandemics.di.module.OffersFragmentModule
import kotlinx.android.synthetic.main.fragment_offers.*
import javax.inject.Inject

class OffersFragment : BaseFragment(), OffersContract.View {

    @Inject
    lateinit var presenter: OffersPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_offers, container, false)
        injectDependencies()
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

    private fun injectDependencies() {
        val fragmentComponent = DaggerOffersFragmentComponent.builder()
            .appComponent(applicationComponent)
            .offersFragmentModule(OffersFragmentModule(this))
            .build()

        fragmentComponent.inject(this)
    }
}
