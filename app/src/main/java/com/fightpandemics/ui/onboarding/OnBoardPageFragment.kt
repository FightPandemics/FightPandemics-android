package com.fightpandemics.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fightpandemics.R

class OnBoardPageFragment : Fragment() {

    private var onBoardItems: ArrayList<OnBoardItem> = ArrayList()
    private var position: Int = 0

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_PAGE_NUMBER = "page_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(pageNumber: Int) = OnBoardPageFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt(ARG_PAGE_NUMBER, pageNumber)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt(ARG_PAGE_NUMBER) ?: 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.onboard_page_fragment, container, false)

        loadData()

        val imageView = rootView.findViewById<ImageView>(R.id.imageView)
        val textOneTv = rootView.findViewById<TextView>(R.id.textView1)
        val textTwoTv = rootView.findViewById<TextView>(R.id.textView2)

        val item = onBoardItems[position]

        imageView.setImageResource(item.imageID)
        textOneTv.text = item.textOne
        textTwoTv.text = item.textTwo

        return rootView
    }

    private fun loadData() {
        val images = listOf(
            R.drawable.onboarding_1_image,
            R.drawable.onboarding_2_image
        )
        val headerTexts = listOf(
            R.string.welcome_to_fightpandemics,
            R.string.find_and_share_support_with_people_near_you
        )
        val descTexts = listOf(
            R.string.first_on_boarding_desc_text,
            R.string.second_on_boarding_desc_text
        )
        for (i in images.indices) {
            val onBoardItem = OnBoardItem(
                images[i],
                resources.getString(headerTexts[i]),
                resources.getString(descTexts[i])
            )
            onBoardItems.add(onBoardItem)
        }
    }
}
