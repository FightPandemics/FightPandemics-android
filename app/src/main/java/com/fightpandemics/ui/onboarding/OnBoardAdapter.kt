package com.fightpandemics.ui.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fightpandemics.R

class OnBoardAdapter(private val context: Context, private val onBoardItems: ArrayList<OnBoardItem>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return onBoardItems.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.onboard_item_view, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val textOneTv = itemView.findViewById<TextView>(R.id.textView1)
        val textTwoTv = itemView.findViewById<TextView>(R.id.textView2)
        val item = onBoardItems[position]
        imageView.setImageResource(item.imageID)
        textOneTv.text = item.textOne
        textTwoTv.text = item.textTwo
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}

class OnBoardingAdapter(
    list: ArrayList<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}