package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import kotlinx.android.synthetic.main.donation_item.*
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.email_item.*
import kotlinx.android.synthetic.main.fragment_name_edit_profile.*
import kotlinx.android.synthetic.main.location_item.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment_content.*
import kotlinx.android.synthetic.main.profile_toolbar.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class EditProfileNameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val editProfileViewModel: EditProfileViewModel by viewModels { viewModelFactory }


    companion object {
        fun newInstance() = EditProfileNameFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_name_edit_profile, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val name = arguments?.get("name") as String
        et_name.setText(name, TextView.BufferType.EDITABLE)
        toolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}