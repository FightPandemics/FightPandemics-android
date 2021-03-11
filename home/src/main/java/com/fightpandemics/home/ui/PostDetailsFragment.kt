package com.fightpandemics.home.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fightpandemics.core.data.model.post.PostDetail
import com.fightpandemics.core.utils.GlideApp
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.databinding.FragmentPostDetailsBinding
import com.fightpandemics.home.utils.getPostCreated
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class PostDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val postDetailsViewModel: PostDetailsViewModel
        by viewModels({ requireParentFragment() }) { viewModelFactory }

    private var _binding: FragmentPostDetailsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    val args: PostDetailsFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val postId = args.postId
        postDetailsViewModel.getPost(postId = postId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpObservers() {
        postDetailsViewModel.postDetailState.observe(
            viewLifecycleOwner,
            { state ->
                when {
                    state.isLoading -> bindLoading(state.isLoading)
                    state.postDetailResponse != null -> {
                        bindLoading(state.isLoading)
                        bindPost(state.postDetailResponse.post)
                    }
                    state.errorMessage != null -> {
                        bindLoading(state.isLoading)
                    }
                }
            }
        )
    }

    private fun bindPost(post: PostDetail) {
        binding.postLayout.apply {
            objective.text = post.objective
            "Posted ${getPostCreated(post.createdAt)} ago".also { timePosted.text = it }
            postTitle.text = post.title
            postContent.text = post.content
            likesCount.text = post.likesCount.toString()
            commentsCount.text = post.commentsCount.toString()
            userFullName.text = post.author.name
            "${post.author.location.city}, ${post.author.location.state}, ${post.author.location.country}".also {
                binding.postLayout.userLocation.text = it
            }

            val imageView = binding.postLayout.userAvatar
            val author = post.author
            if (author.photo.isBlank()) {
                imageView.setInitials(author.name.substring(0, 1))
                imageView.invalidate()
            } else {
                GlideApp
                    .with(requireContext())
                    .load(author.photo)
                    .centerCrop()
                    .into(imageView)
            }
        }
    }

    private fun bindLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.postDetailsContent.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.postDetailsContent.visibility = View.VISIBLE
        }
    }
}
