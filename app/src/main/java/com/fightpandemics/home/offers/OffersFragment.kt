package com.fightpandemics.home.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fightpandemics.R

class OffersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_offers, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        textView.text = "OFFERS"

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(): OffersFragment {
            return OffersFragment()
        }
    }
}
