package com.fightpandemics.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.fightpandemics.App
import com.fightpandemics.R
import com.fightpandemics.di.component.DaggerHomeActivityComponent
import com.fightpandemics.di.component.HomeActivityComponent
import com.fightpandemics.di.module.HomeActivityContextModule
import com.fightpandemics.di.module.HomeActivityMvpModule
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_tab_appbar.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.View {

    private lateinit var activityComponent: HomeActivityComponent

    @Inject
    lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        injectDependencies()
        setupUi()
    }

    private fun injectDependencies() {
        val applicationComponent = (application as App).appComponent
        activityComponent = DaggerHomeActivityComponent.builder()
            .homeActivityContextModule(HomeActivityContextModule(this))
            .homeActivityMvpModule(HomeActivityMvpModule(this))
            .appComponent(applicationComponent)
            .build()

        activityComponent.injectHomeActivity(this)
    }

    private fun setupUi() {
        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager
        )
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        fab.setOnClickListener(this::fabAction)
    }

    private fun fabAction(view: View) {
        // TODO
    }

    override fun setPresenter(presenter: HomeContract.Presenter) {
        TODO("Not yet implemented")
    }
}