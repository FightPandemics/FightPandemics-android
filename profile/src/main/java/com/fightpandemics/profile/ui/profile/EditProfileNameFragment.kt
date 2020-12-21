package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.*
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import kotlinx.android.synthetic.main.edit_profile_name_fragment.*
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
        return inflater.inflate(R.layout.edit_profile_name_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val name = arguments?.get("name") as String
        et_name.setText(name, TextView.BufferType.EDITABLE)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val currentAccount = arguments?.get("profile") as IndividualProfileResponse
        name_save_button.setOnClickListener {
//            TODO("todo what to do about separating first from last name???")
            val new_name = et_name.text.toString()
            editProfileViewModel.updateAccount(
                PatchIndividualAccountRequest(
                    firstName = new_name,
                    hide = currentAccount.hide,
                    lastName = new_name,
                    location = currentAccount.location,
                    needs = currentAccount.needs,
                    objectives = currentAccount.objectives
                )
            )
        }


    }
}