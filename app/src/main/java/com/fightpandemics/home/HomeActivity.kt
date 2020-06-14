package com.fightpandemics.home

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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

    private var isFabOpen = false

    private lateinit var fab_open : Animation
    private lateinit var fab_close : Animation
    private lateinit var rotate_forward : Animation
    private lateinit var rotate_backward : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        injectDependencies()
        fab_open = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(applicationContext,R.anim.fab_close)
        rotate_forward = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_forward)
        rotate_backward = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_backward)
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
        if(isFabOpen){
            fab.startAnimation(rotate_backward)
            fabCreateAsOrg.startAnimation(fab_close)
            fabCreateAsIndiv.startAnimation(fab_close)
            fabCreateAsOrg.isClickable = false
            fabCreateAsIndiv.isClickable = false
            fabCreateAsIndiv.visibility = View.INVISIBLE
            fabCreateAsOrg.visibility = View.INVISIBLE
            isFabOpen = false
        } else {
            fab.startAnimation(rotate_forward)
            fabCreateAsOrg.startAnimation(fab_open)
            fabCreateAsIndiv.startAnimation(fab_open)
            fabCreateAsOrg.isClickable = true
            fabCreateAsIndiv.isClickable = true
            fabCreateAsIndiv.visibility = View.VISIBLE
            fabCreateAsOrg.visibility = View.VISIBLE
            isFabOpen = true
        }
    }
}