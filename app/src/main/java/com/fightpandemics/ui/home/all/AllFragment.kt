package com.fightpandemics.ui.home.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fightpandemics.R
import kotlinx.android.synthetic.main.fragment_all.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllFragment : Fragment() {

    private val viewModel: AllViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent()
    }

    companion object {
        @JvmStatic
        fun newInstance(): AllFragment {
            return AllFragment()
        }
    }

    private fun setContent() {
        section_label.text = viewModel.setContent()
    }
}