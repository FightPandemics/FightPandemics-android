package com.fightpandemics.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.fightpandemics.R
import com.fightpandemics.base.BaseActivity
import com.fightpandemics.util.applyStyle
import com.google.android.material.bottomnavigation.BottomNavigationView
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


    lateinit var toolbar : ActionBar

    private lateinit var menu : BottomNavigationView

    private lateinit var home : MenuItem
    private lateinit var search : MenuItem
    private lateinit var inbox : MenuItem
    private lateinit var profile : MenuItem
    private lateinit var dot : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        injectDependencies()
        initAnims()
        initFabActions()
        setupUi()
        initBottomNavBar()
        initIcons()
        changeDotLocation(1)
    }

    private fun initBottomNavBar() {
        menu = findViewById(R.id.bottomNavBar)
        menu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun initIcons() {
        home = menu.menu.getItem(0)
        search = menu.menu.getItem(1)
        inbox = menu.menu.getItem(2)
        profile = menu.menu.getItem(3)
    }

    private fun initAnims() {
        fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(applicationContext,R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_backward)
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
        if(isFabOpen){
            hideFabActions()
        } else {
            showFabActions()
        }
    }

    private fun hideFabActions() {
        fab.startAnimation(rotateBackward)
        fabCreateAsOrg.startAnimation(fabClose)
        fabCreateAsIndiv.startAnimation(fabClose)
        fabCreateAsOrg.isClickable = false
        fabCreateAsIndiv.isClickable = false
        fabCreateAsIndiv.visibility = View.INVISIBLE
        fabCreateAsOrg.visibility = View.INVISIBLE
        isFabOpen = false
    }

    private fun showFabActions() {
        fab.startAnimation(rotateForward)
        fabCreateAsOrg.startAnimation(fabOpen)
        fabCreateAsIndiv.startAnimation(fabOpen)
        fabCreateAsOrg.isClickable = true
        fabCreateAsIndiv.isClickable = true
        fabCreateAsIndiv.visibility = View.VISIBLE
        fabCreateAsOrg.visibility = View.VISIBLE
        isFabOpen = true
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                handleBottomNavSelection(0)
                return@OnNavigationItemSelectedListener true
            }

            R.id.search -> {
                handleBottomNavSelection(1)
                return@OnNavigationItemSelectedListener true
            }

            R.id.inbox -> {
                handleBottomNavSelection(2)
                return@OnNavigationItemSelectedListener true
            }

            R.id.profile -> {
                handleBottomNavSelection(3)
                return@OnNavigationItemSelectedListener true
            }

            hideOptions()
        } else {
            showOptions()
        }
        false
    }

    private fun handleBottomNavSelection(index: Int) {
        when (index) {
            0 -> {
                changeDotLocation(index+1)
                home.title = ""
                search.title = "Search"
                inbox.title = "Inbox"
                profile.title = "Profile"
            }
            1 -> {
                changeDotLocation(index+1)
                home.title = "Home"
                search.title = ""
                inbox.title = "Inbox"
                profile.title = "Profile"
            }
            2 -> {
                changeDotLocation(index+1)
                home.title = "Home"
                search.title = "Search"
                inbox.title = ""
                profile.title = "Profile"
            }
            else -> {
                changeDotLocation(index+1)
                home.title = "Home"
                search.title = "Search"
                inbox.title = "Inbox"
                profile.title = ""
            }
        }
    }

    private fun changeDotLocation(location : Int) {
        dot = findViewById<ImageView>(R.id.dot)
        val layoutParams = dot.layoutParams as ViewGroup.MarginLayoutParams
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val density = resources.displayMetrics.density
        when (location) {
            //long decimals below so that with any screen size, the dot will be placed correctly instead of hardcoded values
            1 -> layoutParams.setMargins(((0.118333333 * width).toInt()), dot.marginTop, dot.marginRight, dot.marginBottom)
            2 -> layoutParams.setMargins(((0.37083333 * width).toInt()), dot.marginTop, dot.marginRight, dot.marginBottom)
            3 -> layoutParams.setMargins(((0.61666667 * width).toInt()), dot.marginTop, dot.marginRight, dot.marginBottom)
            else -> layoutParams.setMargins(((0.86666667 * width).toInt()), dot.marginTop, dot.marginRight, dot.marginBottom)
        }

        dot.layoutParams = layoutParams
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