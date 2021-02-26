package com.fightpandemics.createpost.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.post.CreatePostRequest
import com.fightpandemics.core.data.model.post.CreatePostResponse
import com.fightpandemics.core.utils.GlideApp
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.core.utils.capitalizeFirstLetter
import com.fightpandemics.core.utils.userInitials
import com.fightpandemics.createpost.R
import com.fightpandemics.createpost.dagger.inject
import com.fightpandemics.createpost.databinding.FragmentCreatePostBinding
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_create_post.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CreatePostFragment : Fragment() {

    @Inject
    lateinit var createPostViewModelFactory: ViewModelFactory
    private val createPostViewModel: CreatePostViewModel by viewModels { createPostViewModelFactory }

    private var fragmentCreatePostBinding: FragmentCreatePostBinding? = null
    private lateinit var titleTextWatcher: TextWatcher
    private lateinit var descriptionTextWatcher: TextWatcher
    private val chipTexts: ArrayList<String> = ArrayList()

    @InternalCoroutinesApi
    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCreatePostBinding
            .inflate(inflater, container, false)
        fragmentCreatePostBinding = binding
        return binding.root
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        setTextWatchers()
        setupViews()
        observeDurationBottomDialog()
        observeVisibilityBottomDialog()
        observeTagBottomDialog()
        displayChosenTags()
        observeFields()
        observeNetworkResponse()
        createPostViewModel.getIndividualProfile()
        createPostViewModel.individualProfile.observe(viewLifecycleOwner) { profile ->
            getIndividualProfileListener(profile)
        }
    }

    @ExperimentalCoroutinesApi
    private fun getIndividualProfileListener(profile: CreatePostViewModel.IndividualProfileViewState) {
        when (profile.error) {
            null -> {
                name.text = profile.firstName?.capitalizeFirstLetter() + " " + profile.lastName?.capitalizeFirstLetter()

                if (profile.imgUrl == null || profile.imgUrl.toString().isBlank()) {
                    user_avatar.setInitials(userInitials(profile.firstName, profile.lastName))
                    user_avatar.invalidate()
                } else {
                    GlideApp
                        .with(requireContext())
                        .load(profile.imgUrl)
                        .centerCrop()
                        .into(user_avatar)
                }
            }
        }
    }

    override fun onDestroyView() {
        fragmentCreatePostBinding = null
        super.onDestroyView()
    }

    private fun setupToolBar() {
        fragmentCreatePostBinding!!.createPostToolbar.createPostToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    private fun setupViews() {
        fragmentCreatePostBinding!!.toggleBt1.isChecked = true
        fragmentCreatePostBinding!!.etTitle.addTextChangedListener(titleTextWatcher)
        fragmentCreatePostBinding!!.etDescription.addTextChangedListener(descriptionTextWatcher)
        fragmentCreatePostBinding!!.name.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_selectOrganization)
        }
        fragmentCreatePostBinding!!.people.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_selectVisibilityFragment)
        }
        fragmentCreatePostBinding!!.month.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_selectDurationFragment)
        }
        fragmentCreatePostBinding!!.tag.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_selectTagFragment)
        }
        fragmentCreatePostBinding!!.post.setOnClickListener {
            postContent()
            et_title.text = null
            et_description.text = null
        }
    }

    private fun setTextWatchers() {
        titleTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createPostViewModel.titleNotEmpty.value = !s.isNullOrBlank() || !s.isNullOrEmpty()
                createPostViewModel.setDataFilled()

                if (s!!.length >= 60) {
                    fragmentCreatePostBinding!!.error.visibility = View.VISIBLE
                } else {
                    fragmentCreatePostBinding!!.error.visibility = View.GONE
                }
            }
        }
        descriptionTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createPostViewModel.descriptionNotEmpty.value = !s.isNullOrBlank() || !s.isNullOrEmpty()
                createPostViewModel.setDataFilled()
            }
        }
    }

    private fun observeDurationBottomDialog() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.createPostFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("duration")) {
                when (navBackStackEntry.savedStateHandle.get<String>("duration")) {
                    "A day" -> {
                        fragmentCreatePostBinding!!.month.text = getString(R.string.for_1_day)
                    }
                    "A week" -> {
                        fragmentCreatePostBinding!!.month.text = getString(R.string.for_1_week)
                    }
                    "A month" -> {
                        fragmentCreatePostBinding!!.month.text = getString(R.string.for_1_month)
                    }
                    else -> {
                        fragmentCreatePostBinding!!.month.text = getString(R.string.forever)
                    }
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_PAUSE) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            }
        )
    }

    private fun observeVisibilityBottomDialog() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.createPostFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("visibility")) {
                when (navBackStackEntry.savedStateHandle.get<String>("visibility")) {
                    "My state" -> {
                        fragmentCreatePostBinding!!.people.text = getString(R.string.people_in_my_state)
                    }
                    "My city" -> {
                        fragmentCreatePostBinding!!.people.text = getString(R.string.people_in_my_city)
                    }
                    "My country" -> {
                        fragmentCreatePostBinding!!.people.text = getString(R.string.people_in_my_country)
                    }
                    else -> {
                        fragmentCreatePostBinding!!.people.text = getString(R.string.worldwide)
                    }
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_PAUSE) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            }
        )
    }

    private fun observeTagBottomDialog() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.createPostFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("tag1")) {
                chipTexts.add(navBackStackEntry.savedStateHandle.get<String>("tag1")!!)
                chipTexts.add(navBackStackEntry.savedStateHandle.get<String>("tag2")!!)
                chipTexts.add(navBackStackEntry.savedStateHandle.get<String>("tag3")!!)
            }
            displayChosenTags()
            createPostViewModel.isTagSet.value = tag_chip_group.childCount > 0
            createPostViewModel.setDataFilled()
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_PAUSE) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            }
        )
    }

    @SuppressLint("InflateParams")
    private fun displayChosenTags() {
        if (tag_chip_group.childCount > 0) {
            tag_chip_group.removeAllViews()
        }

        for (text in chipTexts) {
            val chip = layoutInflater.inflate(R.layout.item_create_post_tag, null, false) as Chip
            chip.text = text
            tag_chip_group.addView(chip)
        }
        chipTexts.clear()
    }

    private fun observeFields() {
        createPostViewModel.allDataFilled.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    fragmentCreatePostBinding!!.post.apply {
                        isEnabled = true
                        setBackgroundColor(resources.getColor(R.color.colorPrimary))
                    }
                } else {
                    fragmentCreatePostBinding!!.post.apply {
                        isEnabled = false
                        setBackgroundColor(resources.getColor(R.color.fightPandemicsPerano))
                    }
                }
            }
        )
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    private fun postContent() {
        val visibility =
            if (fragmentCreatePostBinding!!.people.text.toString().toLowerCase(Locale.ROOT)
                .contains("people in my ")
            )
                fragmentCreatePostBinding!!.people.text.toString().toLowerCase(Locale.ROOT)
                    .replace("people in my ", "")
            else fragmentCreatePostBinding!!.people.text.toString().toLowerCase(Locale.ROOT)
        val expireAt =
            if (fragmentCreatePostBinding!!.month.text.toString().toLowerCase(Locale.ROOT)
                .contains("for 1 ")
            )
                fragmentCreatePostBinding!!.month.text.toString().toLowerCase(Locale.ROOT)
                    .replace("for 1 ", "") else
                "forever"
        val objective = if (fragmentCreatePostBinding!!.toggleBt1.isChecked)
            fragmentCreatePostBinding!!.toggleBt1.text.toString().toLowerCase(Locale.ROOT) else
            fragmentCreatePostBinding!!.toggleBt2.text.toString().toLowerCase(Locale.ROOT)

        val createPostRequest = CreatePostRequest(
            title = fragmentCreatePostBinding!!.etTitle.text.toString().trim(),
            content = fragmentCreatePostBinding!!.etDescription.text.toString().trim(),
            types = listOf((tag_chip_group[0] as Chip).text.toString(), (tag_chip_group[1] as Chip).text.toString(), (tag_chip_group[2] as Chip).text.toString()),
            visibility = visibility,
            expireAt = expireAt,
            objective = objective,
        )
        createPostViewModel.postContent(createPostRequest)
    }

    private fun observeNetworkResponse() {
        createPostViewModel.networkResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is CreatePostResponse -> {
                        Timber.tag("CREATE_POST_NETWRK_RESP").e(Gson().toJson(it))
                        showDialog()
                    }
                    is Exception -> {
                        Timber.tag("CREATE_POST_ERROR_RESP").e(Gson().toJson(it))
                        Snackbar.make(fragmentCreatePostBinding!!.root, it.message!!, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(R.color.colorPrimary)).show()
                    }
                }
            }
        )
    }

    @SuppressLint("InflateParams")
    private fun showDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_view_post, null, false)
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setView(view)
        alertDialog.setCancelable(false)
        val builder = alertDialog.show()
        view.findViewById<ImageView>(R.id.btn_view_post).setOnClickListener {
            builder.dismiss()
            findNavController().navigateUp()
        }
        view.findViewById<ImageView>(R.id.btn_view_post).setOnClickListener {
            builder.dismiss()
            findNavController().navigateUp()
        }
    }
}
