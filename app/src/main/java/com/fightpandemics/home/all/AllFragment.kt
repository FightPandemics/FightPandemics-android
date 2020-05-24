package com.fightpandemics.home.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.R
import com.fightpandemics.base.BaseFragment
import com.fightpandemics.di.component.DaggerAllFragmentComponent
import com.fightpandemics.di.module.AllFragmentModule
import kotlinx.android.synthetic.main.fragment_all.*
import javax.inject.Inject

class AllFragment : BaseFragment(), AllContract.View {

    @Inject
    lateinit var presenter: AllPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_all, container, false)
        injectDependencies()
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

    private fun injectDependencies() {
        val fragmentComponent = DaggerAllFragmentComponent.builder()
            .appComponent(applicationComponent)
            .allFragmentModule(AllFragmentModule(this))
            .build()

        fragmentComponent.inject(this)
    }
}