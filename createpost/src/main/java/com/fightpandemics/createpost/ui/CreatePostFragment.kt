package com.fightpandemics.createpost.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.posts.Author
import com.fightpandemics.core.data.model.posts.ExternalLinks
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.createpost.R
import com.fightpandemics.createpost.dagger.inject
import com.fightpandemics.createpost.data.model.CreatePostRequest
import com.fightpandemics.createpost.databinding.FragmentCreatePostBinding
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_create_post.*
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreatePostBinding
            .inflate(inflater, container, false)
        fragmentCreatePostBinding = binding
        return binding.root
    }

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
        }
    }

    private fun setTextWatchers() {
        titleTextWatcher = object: TextWatcher {
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
        descriptionTextWatcher = object: TextWatcher {
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
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
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
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
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
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
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
        createPostViewModel.allDataFilled.observe(viewLifecycleOwner, Observer {
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
        })
    }

    private fun postContent() {
        // Todo: Get the current logged in user and use his id as the actorId or use his organization id
        //val actorId = fragmentCreatePostBinding!!.name.text.toString()
        val expireAt =
            if (fragmentCreatePostBinding!!.month.text.toString().toLowerCase(Locale.ROOT)
                    .contains("for 1 ")
            )
                fragmentCreatePostBinding!!.month.text.toString().toLowerCase(Locale.ROOT)
                    .replace("for 1 ", "") else
                "never"
        val objective = if (fragmentCreatePostBinding!!.toggleBt1.isChecked)
            fragmentCreatePostBinding!!.toggleBt1.text.toString().toLowerCase(Locale.ROOT) else
            fragmentCreatePostBinding!!.toggleBt2.text.toString().toLowerCase(Locale.ROOT)
        val visibility =
            if (fragmentCreatePostBinding!!.people.text.toString().toLowerCase(Locale.ROOT)
                    .contains("people in my ")
            )
                fragmentCreatePostBinding!!.people.text.toString().toLowerCase(Locale.ROOT)
                    .replace("people in my ", "")
            else fragmentCreatePostBinding!!.people.text.toString().toLowerCase(Locale.ROOT)

        val createPostRequest = CreatePostRequest(
            actorId = "YTYHIJIOKL54974OPIUHIUGYTFGRTRTUHJUIUYHYTHJIK",//Dummy data
            content = fragmentCreatePostBinding!!.etDescription.text.toString().trim(),
            expireAt = expireAt,
            externalLinks = ExternalLinks("test website"),
            language = listOf(Locale.getDefault().language.toLowerCase(Locale.ROOT)),
            objective = objective,
            title = fragmentCreatePostBinding!!.etTitle.text.toString().trim(),
            types = listOf((tag_chip_group[0] as Chip).text.toString(), (tag_chip_group[1] as Chip).text.toString(), (tag_chip_group[2] as Chip).text.toString()),
            visibility = visibility
        )
        //Timber.tag("CREATE_POST_OBJECT").e(Gson().toJson(createPostRequest))
        createPostViewModel.postContent(createPostRequest)
    }
}