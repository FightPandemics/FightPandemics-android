package com.fightpandemics.createpost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.createpost.R
import com.fightpandemics.createpost.adapter.Organization
import com.fightpandemics.createpost.adapter.OrganizationAdapter
import com.fightpandemics.createpost.databinding.FragmentSelectOrganizationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_select_organization.*

class SelectOrganizationFragment : BottomSheetDialogFragment() {

    private var fragmentSelectOrganizationBinding: FragmentSelectOrganizationBinding? = null
    private var organizationArray: Array<Organization>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectOrganizationBinding.inflate(inflater, container, false)
        fragmentSelectOrganizationBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildData()
        setupView()
        setupRv()
    }

    private fun setupView() {
        fragmentSelectOrganizationBinding!!.userCard.setOnClickListener {
            user_radio_bt.isChecked = !user_radio_bt.isChecked
        }
        fragmentSelectOrganizationBinding!!.add.setOnClickListener {
            dismiss()
        }
    }

    private fun buildData() {
        val org1 = Organization(R.drawable.ic_plus_sign, "Notion IO")
        val org2 = Organization(R.drawable.ic_tag, "FightPandemics")
        val org3 = Organization(R.drawable.ic_plus_sign, "Amazon Ltd")
        val org4 = Organization(R.drawable.ic_tag, "Spaces Plc")
        val org5 = Organization(R.drawable.ic_plus_sign, "Channels TV")
        organizationArray = arrayOf(org1, org2, org3, org4, org5)
    }

    private fun setupRv() {
        fragmentSelectOrganizationBinding!!.rvOrganization.layoutManager = LinearLayoutManager(requireContext())
        fragmentSelectOrganizationBinding!!.rvOrganization.adapter = OrganizationAdapter(organizationArray!!)
    }
}