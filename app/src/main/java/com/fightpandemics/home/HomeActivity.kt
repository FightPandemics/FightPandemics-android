package com.fightpandemics.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.fightpandemics.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_tab_appbar.*

class HomeActivity : AppCompatActivity(), HomeContract.View {

    private lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setPresenter(HomePresenter(this))
        setupUi()
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
        this.presenter = presenter
    }
}