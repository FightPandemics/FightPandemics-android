package com.fightpandemics.home

import android.os.Bundle
import android.widget.TextView
import com.fightpandemics.R
import com.fightpandemics.base.BaseActivity
import com.fightpandemics.di.component.DaggerHomeActivityComponent
import com.fightpandemics.di.module.HomeActivityModule
import com.fightpandemics.util.applyStyle
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_tab_appbar.*
import javax.inject.Inject


class HomeActivity : BaseActivity(), HomeContract.View {

    @Inject
    lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        injectDependencies()
        setupUi()
    }

    private fun injectDependencies() {
        val activityComponent = DaggerHomeActivityComponent.builder()
            .appComponent(applicationComponent)
            .homeActivityModule(HomeActivityModule(this))
            .build()

        activityComponent.inject(this)
    }

    private fun setupUi() {
        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager
        )
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        tabs.addOnTabSelectedListener(OnTabSelected())

        for (i in 0 until tabs.tabCount) {
            val title = tabs.getTabAt(i)?.text
            tabs.getTabAt(i)?.setCustomView(R.layout.item_tab_title)
            val tabTitle = tabs.getTabAt(i)?.customView?.findViewById<TextView>(R.id.tv_title)
            tabTitle?.text = title
            tabTitle?.applyStyle(tabTitle.isSelected)
        }

        fab.setOnClickListener { fabAction() }
    }

    private fun fabAction() {
        // TODO
    }
}