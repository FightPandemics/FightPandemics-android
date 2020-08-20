package com.fightpandemics.ui.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.fightpandemics.R
import com.fightpandemics.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    private lateinit var bottomNavigationView :BottomNavigationView

    private var isFabOpen = false
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation

    private lateinit var home: MenuItem
    private lateinit var search: MenuItem
    private lateinit var inbox: MenuItem
    private lateinit var profile: MenuItem
    private lateinit var dot: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavBar)

        initFabActions()
        //setupUi()
        //initBottomNavBar()
        initIcons()
        //handleBottomNavSelection(0)


        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        setUpNavDestinationChangeListener()
    }

    private fun initIcons() {
        home = bottomNavigationView.menu.getItem(0)
        search = bottomNavigationView.menu.getItem(1)
        inbox = bottomNavigationView.menu.getItem(2)
        profile = bottomNavigationView.menu.getItem(3)
    }

    private fun setUpNavDestinationChangeListener() {
        val destinationChangeListener = DestinationChangeListener()
        currentNavController?.observe(this) {
            it.addOnDestinationChangedListener(destinationChangeListener)
        }
    }

    inner class DestinationChangeListener() : NavController.OnDestinationChangedListener {
        override fun onDestinationChanged(
            controller: NavController,
            destination: NavDestination,
            arguments: Bundle?
        ) {
            //findViewById<Toolbar>(R.id.toolbar).title = destination.label
            //findViewById<CollapsingToolbarLayout>(R.id.collasping_toolbar).isTitleEnabled = false;
            //findViewById<CollapsingToolbarLayout>(R.id.collasping_toolbar).title = destination.label
            if (/*destination.id == R.id.detailsFragment*/ false) {
                //findViewById<AppBarLayout>(R.id.app_bar).setExpanded(false)
                hideBottomTabs()
            } else {
                showBottomTabs()
                handleBottomNavSelection(0)
                Timber.e(destination.id.toString())
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavBar)

        val navGraphIds = listOf(
            R.navigation.nav_home,
            R.navigation.nav_search,
            R.navigation.nav_inbox,
            R.navigation.nav_profile
        )

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.mainHostFragment,
            intent = intent
        )

        controller.observe(this, Observer { navController ->
            //setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }


    private fun hideBottomTabs() {
        if (findViewById<BottomNavigationView>(R.id.bottomNavBar).visibility == View.VISIBLE) {
            //findViewById<BottomNavigationView>(R.id.bottomNavBar).slideDown()
        }
    }

    private fun showBottomTabs() {
        if (findViewById<BottomNavigationView>(R.id.bottomNavBar).visibility != View.VISIBLE) {
            //findViewById<BottomNavigationView>(R.id.bottomNavBar).slideUp()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun initFabActions() {
        fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(applicationContext,R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_backward)
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
        fabCreateAsIndividual.startAnimation(fabClose)
        fabCreateAsOrg.isClickable = false
        fabCreateAsIndividual.isClickable = false
        fabCreateAsIndividual.visibility = View.INVISIBLE
        fabCreateAsOrg.visibility = View.INVISIBLE
        isFabOpen = false
    }

    private fun showFabActions() {
        fab.startAnimation(rotateForward)
        fabCreateAsOrg.startAnimation(fabOpen)
        fabCreateAsIndividual.startAnimation(fabOpen)
        fabCreateAsOrg.isClickable = true
        fabCreateAsIndividual.isClickable = true
        fabCreateAsIndividual.visibility = View.VISIBLE
        fabCreateAsOrg.visibility = View.VISIBLE
        isFabOpen = true
    }

    private fun handleBottomNavSelection(index: Int) {
        home.title = if(index == 0) "" else getString(R.string.home)
        search.title = if(index == 1) "" else getString(R.string.search)
        inbox.title = if(index == 2) "" else getString(R.string.inbox)
        profile.title = if(index == 3) "" else getString(R.string.profile)
        changeDotLocation(index + 1)
    }

    private fun changeDotLocation(location : Int) {
        dot = findViewById<ImageView>(R.id.dot)
        val layoutParams = dot.layoutParams as ViewGroup.MarginLayoutParams
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        when (location) {
            //long decimals below so that with any screen size, the dot will be placed correctly instead of hardcoded values
            1 -> {
                layoutParams.setMargins(((0.118333333 * width).toInt()), dot.marginTop, dot.marginRight, dot.marginBottom)
            }
            2 -> {
                layoutParams.setMargins(((0.37083333 * width).toInt()), dot.marginTop, dot.marginRight, dot.marginBottom)
            }
            3 -> {
                layoutParams.setMargins(((0.61666667 * width).toInt()), dot.marginTop, dot.marginRight, dot.marginBottom)
            }
            else -> {
                layoutParams.setMargins(((0.86666667 * width).toInt()), dot.marginTop, dot.marginRight, dot.marginBottom)
            }
        }
        dot.layoutParams = layoutParams
    }
}
