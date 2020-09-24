package com.fightpandemics.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.fightpandemics.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_fragment)
    }

    fun launchActivity(
        clazz: Class<*>,
        finishAll: Boolean,
        extra: Bundle?,
        requestCode: Int?
    ) {
        Intent(this, clazz).apply {
            if (finishAll) {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            extra?.run { putExtras(this) }
        }.run {
            if (requestCode == null) {
                startActivity(this)
            } else {
                startActivityForResult(this, requestCode)
            }
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean, container: Int) {
        if (!isFinishing) {
            supportFragmentManager?.beginTransaction()?.apply {
                replace(container, fragment, fragment::class.java.simpleName)
                if (addToBackStack) {
                    addToBackStack(null)
                }
            }?.commitAllowingStateLoss()
        }
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager?.beginTransaction()?.apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.fragment_container, fragment, fragment::class.java.simpleName)
            if (addToBackStack) {
                addToBackStack(null)
            }
        }
        transaction?.commitAllowingStateLoss()
    }

}