package com.fightpandemics.home.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.R
import com.fightpandemics.base.BaseFragment
import com.fightpandemics.di.component.DaggerRequestsFragmentComponent
import com.fightpandemics.di.module.RequestsFragmentModule
import kotlinx.android.synthetic.main.fragment_offers.*
import javax.inject.Inject

class RequestsFragment : BaseFragment(), RequestsContract.View {

    @Inject
    lateinit var presenter: RequestsPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_requests, container, false)
        injectDependencies()
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

    private fun injectDependencies() {
        val fragmentComponent = DaggerRequestsFragmentComponent.builder()
            .appComponent(applicationComponent)
            .requestsFragmentModule(RequestsFragmentModule(this))
            .build()

        fragmentComponent.inject(this)
    }
}