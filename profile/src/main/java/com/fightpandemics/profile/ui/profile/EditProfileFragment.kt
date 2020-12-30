package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.utils.GlideApp
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.util.capitalizeFirstLetter
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment_content.*
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
    private val profileViewModel: ProfileViewModel by activityViewModels { viewModelFactory }

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
    override fun onStart() {
        super.onStart()
        relativeLayoutName?.setOnClickListener {
            findNavController().navigate(
                com.fightpandemics.R.id.action_editProfileFragment_to_changeNameFragment,
            )
        }
        relativeLayoutSocial?.setOnClickListener {
            findNavController().navigate(
                com.fightpandemics.R.id.action_editProfileFragment_to_changeSocialFragment,
            )
        }
        rlAboutMe?.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_editProfileFragment_to_changeAboutFragment)
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        profileViewModel.individualProfile.observe(viewLifecycleOwner, { profile ->
            when {
                profile.isLoading -> {
                    //TODO
                }
                !profile.isLoading -> {
                    nameValue?.text = profile.firstName?.capitalizeFirstLetter() + " " + profile.lastName?.capitalizeFirstLetter()
                    if(profile.imgUrl == null || profile.imgUrl.isBlank()){
                        pivAvatar.setInitials(profile?.firstName?.substring(0,1)?.toUpperCase() + profile?.lastName?.split(" ")?.last()?.substring(0,1)?.toUpperCase())
                        pivAvatar.invalidate()
                    }else{
                        GlideApp
                            .with(requireContext())
                            .load(profile.imgUrl)
                            .centerCrop()
                            .into(pivAvatar)
                    }
                }

            }
        })
    }

}