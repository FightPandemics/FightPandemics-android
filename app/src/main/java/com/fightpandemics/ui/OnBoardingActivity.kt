package com.fightpandemics.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.fightpandemics.R
import kotlinx.android.synthetic.main.onboard_item_view.view.*

class OnBoardingActivity : AppCompatActivity() {

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
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
        setPageViewController()
    }

    private fun loadData() {
        val images = listOf(R.drawable.onboarding_1_image, R.drawable.onboarding_2_image)
        val textHeader = listOf("Welcome to FightPandemics", "A place you can offer and get help")
        val textDesc = listOf("Welcome to FightPandemics, Welcome to FightPandemics", "A place you can offer and get help, A place you can offer and get help")
        for (i in images.indices) {
            val onBoardItem = OnBoardItem(images[i], textHeader[i], textDesc[i])
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
            params.setMargins(6, 0, 6, 0)
            pagerIndicator.addView(dots[i], params)
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_item_dot))
    }
}

class OnBoardAdapter(private val context: Context, private val onBoardItems: ArrayList<OnBoardItem>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return onBoardItems.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(context).inflate(R.layout.onboard_item_view, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val textOne = itemView.findViewById<TextView>(R.id.textView1)
        val textTwo = itemView.findViewById<TextView>(R.id.textView2)
        val item = onBoardItems[position]
        imageView.setImageResource(item.imageID)
        textOne.text = item.textOne
        textTwo.text = item.textTwo
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

}

data class OnBoardItem(val imageID: Int, val textOne: String, val textTwo: String)