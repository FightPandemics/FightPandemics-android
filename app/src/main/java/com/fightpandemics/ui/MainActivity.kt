package com.fightpandemics.ui

import android.os.Bundle
import android.util.DisplayMetrics
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
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.fightpandemics.R
import com.fightpandemics.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var dot: ImageView
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation

    private var isFabOpen = false
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavBar)
        dot = findViewById(R.id.dot)

        initFabActions()

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        setUpNavDestinationChangeListener()
    }

    private fun setUpNavDestinationChangeListener() {
        val destinationChangeListener = DestinationChangeListener()
        currentNavController?.observe(this) {
            it.addOnDestinationChangedListener(destinationChangeListener)
        }
    }

    inner class DestinationChangeListener : NavController.OnDestinationChangedListener {
        override fun onDestinationChanged(
            controller: NavController,
            destination: NavDestination,
            arguments: Bundle?
        ) {
            // findViewById<Toolbar>(R.id.toolbar).title = destination.label
            when (destination.id) {
                R.id.homeFragment, R.id.searchFragment, R.id.inboxFragment, R.id.profileFragment
                -> showBottomBar(destination)
                else -> bottomNavigationView.visibility = View.GONE
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
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

        controller.observe(
            this,
            { navController ->
                // setupActionBarWithNavController(navController)
            }
        )
        currentNavController = controller
    }

    private fun showBottomBar(destination: NavDestination) {
        bottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.handleBottomNavSelection(destination)
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun initFabActions() {
        fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_backward)
        fab.setOnClickListener { fabAction() }
    }

    private fun fabAction() {
        if (isFabOpen) hideFabActions() else showFabActions()
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

    private fun BottomNavigationView.handleBottomNavSelection(destination: NavDestination) {
        this.menu.getItem(0).title = if (destination.id == R.id.homeFragment) {
            changeDotLocation(1)
            ""
        } else context.getString(R.string.home)

        this.menu.getItem(1).title = if (destination.id == R.id.searchFragment) {
            changeDotLocation(2)
            ""
        } else context.getString(R.string.search)

        this.menu.getItem(2).title = if (destination.id == R.id.inboxFragment) {
            changeDotLocation(3)
            ""
        } else context.getString(R.string.inbox)

        this.menu.getItem(3).title = if (destination.id == R.id.profileFragment) {
            changeDotLocation(4)
            ""
        } else context.getString(R.string.profile)
    }

    private fun changeDotLocation(location: Int) {
        val layoutParams = dot.layoutParams as ViewGroup.MarginLayoutParams
        val displayMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            this.display?.getRealMetrics(displayMetrics)
        } else windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels
        when (location) {
            // long decimals below so that with any screen size, the dot will be placed correctly instead of hardcoded values
            1 -> {
                layoutParams
                    .setMargins(
                        ((0.118333333 * width).toInt()),
                        dot.marginTop,
                        dot.marginRight,
                        dot.marginBottom
                    )
            }
            2 -> {
                layoutParams.setMargins(
                    ((0.37083333 * width).toInt()),
                    dot.marginTop,
                    dot.marginRight,
                    dot.marginBottom
                )
            }
            3 -> {
                layoutParams.setMargins(
                    ((0.61666667 * width).toInt()),
                    dot.marginTop,
                    dot.marginRight,
                    dot.marginBottom
                )
            }
            else -> {
                layoutParams.setMargins(
                    ((0.86666667 * width).toInt()),
                    dot.marginTop,
                    dot.marginRight,
                    dot.marginBottom
                )
            }
        }
        dot.layoutParams = layoutParams
    }
}
