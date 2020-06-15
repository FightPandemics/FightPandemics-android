package com.fightpandemics.home

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.fightpandemics.R
import com.fightpandemics.base.BaseActivity
import com.fightpandemics.util.applyStyle
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_tab_appbar.*
import javax.inject.Inject


class HomeActivity : BaseActivity(), HomeContract.View {

    @Inject
    lateinit var presenter: HomePresenter

    private var isFabOpen = false

    private lateinit var fabOpen : Animation
    private lateinit var fabClose : Animation
    private lateinit var rotateForward : Animation
    private lateinit var rotateBackward : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initFabActions()
        setupUi()
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
            hideOptions()
        } else {
            showOptions()
        }
    }

    private fun showOptions() {
        fab.startAnimation(rotateForward)
        fabCreateAsOrg.startAnimation(fabOpen)
        fabCreateAsIndividual.startAnimation(fabOpen)
        fabCreateAsOrg.isClickable = true
        fabCreateAsIndividual.isClickable = true
        fabCreateAsIndividual.visibility = View.VISIBLE
        fabCreateAsOrg.visibility = View.VISIBLE
        isFabOpen = true
    }

    private fun hideOptions() {
        fab.startAnimation(rotateBackward)
        fabCreateAsOrg.startAnimation(fabClose)
        fabCreateAsIndividual.startAnimation(fabClose)
        fabCreateAsOrg.isClickable = false
        fabCreateAsIndividual.isClickable = false
        fabCreateAsIndividual.visibility = View.INVISIBLE
        fabCreateAsOrg.visibility = View.INVISIBLE
        isFabOpen = false
    }

    private fun initFabActions() {
        fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(applicationContext,R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_backward)
    }
}