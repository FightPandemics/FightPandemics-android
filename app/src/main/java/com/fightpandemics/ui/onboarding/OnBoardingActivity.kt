package com.fightpandemics.ui.onboarding

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.fightpandemics.R
import com.fightpandemics.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : BaseActivity() {

    private lateinit var pagerIndicator: LinearLayout
    private var dotCount: Int = 0
    private lateinit var dots: ArrayList<ImageView>
    private lateinit var onBoardPager: ViewPager
    private lateinit var skipText: TextView
    private lateinit var adapter: OnBoardAdapter
    private var onBoardItems: ArrayList<OnBoardItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        onBoardPager = findViewById(R.id.view_pager)
        pagerIndicator = findViewById(R.id.view_pager_counter_layout)
        skipText = findViewById(R.id.tv_skip)

        loadData()

        adapter = OnBoardAdapter(this, onBoardItems)
        onBoardPager.adapter = adapter
        onBoardPager.currentItem = 0
        onBoardPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotCount) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this@OnBoardingActivity, R.drawable.non_selected_item_dot))
                    dots[position].setImageDrawable(ContextCompat.getDrawable(this@OnBoardingActivity, R.drawable.selected_item_dot))
                }
            }
        })

        skipText.setOnClickListener {
            //launchActivity(MainActivity::class.java, true,null,null)
        }
        bt_join_now.setOnClickListener {
            //launchActivity(LoginActivity::class.java, false, null, LoginStatusEnum.SIGN_UP.value)
        }
        bt_sign_in.setOnClickListener {
            //launchActivity(LoginActivity::class.java, false, null, LoginStatusEnum.SIGN_UP.value)
        }

        setPageViewController()
    }


    private fun loadData() {
        val images = listOf(R.drawable.onboarding_1_image, R.drawable.onboarding_2_image)
        val headerTexts = listOf(R.string.welcome_to_fightpandemics, R.string.find_and_share_support_with_people_near_you)
        val descTexts = listOf(R.string.first_on_boarding_desc_text, R.string.second_on_boarding_desc_text)
        for (i in images.indices) {
            val onBoardItem = OnBoardItem(images[i], resources.getString(headerTexts[i]), resources.getString(descTexts[i]))
            onBoardItems.add(onBoardItem)
        }
    }

    private fun setPageViewController() {
        dotCount = adapter.count
        dots = ArrayList(dotCount)
        for (i in 0 until dotCount) {
            dots.add(ImageView(this))
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_selected_item_dot))
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(12, 0, 12, 0)
            pagerIndicator.addView(dots[i], params)
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_item_dot))
    }
}

