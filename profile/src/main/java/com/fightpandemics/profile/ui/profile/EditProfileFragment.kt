package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val editProfileViewModel: EditProfileViewModel by viewModels { viewModelFactory }

    companion object {
        fun newInstance() = EditProfileFragment()
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
        return inflater.inflate(R.layout.edit_profile_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        editProfileViewModel.currentProfile = arguments?.get("profile") as IndividualProfileResponse

        nameValue.text = editProfileViewModel.getName()
        relativeLayoutName?.setOnClickListener{
           findNavController().navigate(com.fightpandemics.R.id.action_editProfileFragment_to_changeNameFragment, bundleOf("name" to  editProfileViewModel.getName()))
       }
        relativeLayoutSocial?.setOnClickListener{
            findNavController().navigate(com.fightpandemics.R.id.action_editProfileFragment_to_changeSocialFragment, bundleOf("profile" to editProfileViewModel.currentProfile))
        }
        rlAboutMe?.setOnClickListener{
            findNavController().navigate(com.fightpandemics.R.id.action_editProfileFragment_to_changeAboutFragment)
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }
}