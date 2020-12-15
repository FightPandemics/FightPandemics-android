package com.fightpandemics.profile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fightpandemics.profile.R
import com.fightpandemics.core.utils.ViewModelFactory
import kotlinx.android.synthetic.main.donation_item.*
import kotlinx.android.synthetic.main.email_item.*
import kotlinx.android.synthetic.main.location_item.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_setting_my_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       locationRightChevron?.setOnClickListener{
           findNavController().navigate(com.fightpandemics.R.id.action_editProfileFragment_to_changeLocationFragment)
       }
        donationRightChevron?.setOnClickListener{
            findNavController().navigate(com.fightpandemics.R.id.action_editProfileFragment_to_changeGoalFragment)
        }
        emailRightChevron?.setOnClickListener{
            findNavController().navigate(com.fightpandemics.R.id.action_editProfileFragment_to_changeEmailFragment)
        }
    }
}