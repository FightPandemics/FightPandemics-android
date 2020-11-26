package com.fightpandemics.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupActionBarWithNavController
import com.fightpandemics.R
import com.fightpandemics.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fab: FloatingActionButton
    private lateinit var dot: ImageView
    private lateinit var fabCreateAsOrg: MaterialButton
    private lateinit var fabCreateAsIndividual: MaterialButton
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
        fab = findViewById(R.id.fab)
        fabCreateAsIndividual = findViewById(R.id.fabCreateAsIndiv)
        fabCreateAsOrg = findViewById(R.id.fabCreateAsOrg)
        dot = findViewById(R.id.dot)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        setUpBottomNavigationAndFab()
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

        controller.observe(this) { setupActionBarWithNavController(it) }
        currentNavController = controller
    }

    private fun setUpBottomNavigationAndFab() {
        currentNavController?.observe(this) {
            it.addOnDestinationChangedListener(this@MainActivity)
        }

        fab.apply {
            initFabAction()
            setShowMotionSpecResource(R.animator.fab_show)
            setHideMotionSpecResource(R.animator.fab_hide)
            setOnClickListener {
                fabAction()
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.homeFragment, R.id.searchFragment,
            R.id.inboxFragment, R.id.profileFragment,
            -> {
                showBottomBar(destination)
                dot.visibility = View.VISIBLE
                fab.show()
            }
            R.id.filterFragment, R.id.createPostFragment, R.id.settingFragment -> hideBottomBar()
        }
    }

    private fun showBottomBar(destination: NavDestination) {
        bottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.handleBottomNavSelection(destination)
    }

    private fun hideBottomBar() {
        if (isFabOpen) hideFabActions()
        bottomNavigationView.visibility = View.GONE
        dot.visibility = View.GONE
        fab.hide()

        // Get a handle on the animator that hides the bottom app bar so we can wait to hide
        // the fab and bottom app bar until after it's exit animation finishes.
        bottomNavigationView.animate().setListener(object : AnimatorListenerAdapter() {
            var isCanceled = false
            override fun onAnimationEnd(animation: Animator?) {
                if (isCanceled) return

                // Hide the BottomAppBar to avoid it showing above the keyboard
                // when composing a new email.
                bottomNavigationView.visibility = View.GONE
                dot.visibility = View.GONE
                fab.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {
                isCanceled = true
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun initFabAction() {
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward)
    }

    private fun fabAction() {
        if (isFabOpen) hideFabActions() else showFabActions()
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
