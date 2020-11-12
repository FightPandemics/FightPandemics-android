package com.fightpandemics.home.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.EditDeleteBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    private var editDeleteBinding: EditDeleteBinding? = null

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = EditDeleteBinding.inflate(inflater, container, false)
        editDeleteBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    override fun onDestroyView() {
        editDeleteBinding = null
        super.onDestroyView()
    }

    private fun setUpViews() {
        // We can have cross button on the top right corner for providing elemnet to dismiss the bottom sheet
        //iv_close.setOnClickListener { dismissAllowingStateLoss() }

//         txt_download.setOnClickListener {
//             dismissAllowingStateLoss()
//             Toast.makeText(application, "Download option clicked", Toast.LENGTH_LONG)
//                 .show()
//         }

//         txt_share.setOnClickListener {
//             dismissAllowingStateLoss()
//             Toast.makeText(application, "Share option clicked", Toast.LENGTH_LONG)
//                 .show()
//         }
    }

    companion object {
        @JvmStatic
        fun newInstance(/*bundle: Bundle*/): HomeOptionsBottomSheetFragment {
            val fragment = HomeOptionsBottomSheetFragment()
            //fragment.arguments = bundle
            return fragment
        }
    }
}