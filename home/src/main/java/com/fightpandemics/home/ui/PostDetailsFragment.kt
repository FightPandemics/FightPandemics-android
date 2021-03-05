package com.fightpandemics.home.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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

    private val postDetailsViewModel: PostDetailsViewModel by viewModels({ requireParentFragment() }) { viewModelFactory }

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
                binding.progressBar.progressBar.isVisible = state.isLoading
                state.postDetailResponse?.post?.let { post ->
                    binding.postLayout?.let { layout ->
                        layout.objective.text = post.objective
                        "Posted ${getPostCreated(post.createdAt)} ago".also { layout.timePosted.text = it }
                        layout.postTitle.text = post.title
                        layout.postContent.text = post.content
                        layout.likesCount.text = post.likesCount.toString()
                        layout.commentsCount.text = post.commentsCount.toString()
                        layout.userFullName.text = post.author.name
                        "${post.author.location.city}, ${post.author.location.state}, ${post.author.location.country}".also { binding.postLayout.userLocation.text = it }
                        binding.postLayout.userAvatar?.let { imageView ->
                            post.author?.let { author ->
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
                    }
                }
            }
        )
    }
}
