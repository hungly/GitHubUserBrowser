package io.hung.githubuserbrowser.ui.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.hung.githubuserbrowser.GlideApp
import io.hung.githubuserbrowser.R
import io.hung.githubuserbrowser.Utils
import io.hung.githubuserbrowser.data.SourceResult
import io.hung.githubuserbrowser.databinding.UserDetailFragmentBinding
import io.hung.githubuserbrowser.di.Injectable
import io.hung.githubuserbrowser.di.injectViewModel
import javax.inject.Inject

class UserDetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var imageRequestOptions: RequestOptions

    @Inject
    lateinit var transitionOptions: DrawableTransitionOptions

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: UserDetailFragmentBinding

    private val viewModel: UserDetailViewModel by lazy {
        injectViewModel(viewModelFactory)
    }
    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserDetailFragmentBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getUser(args.selectedUserLogin)
    }

    private fun setupObservers() {
        viewModel.selectedUser.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            when (it.status) {
                SourceResult.Status.SUCCESS -> {
                    it.data?.let { user ->
                        val follower = resources.getQuantityString(R.plurals.follower, user.followers ?: 0)
                        val following = resources.getQuantityString(R.plurals.following, user.following ?: 0)

                        binding.tvUserCommunity.text = getString(R.string.community_template, user.followers ?: 0, follower, user.following ?: 0, following)
                        binding.tvUserCompanyAndLocation.text = getString(R.string.company_location_template, user.company ?: "N/A", user.location ?: "N/A")

                        GlideApp.with(requireContext())
                            .load(user.avatarUrl)
                            .apply(imageRequestOptions)
                            .transition(transitionOptions)
                            .into(binding.ivAvatar)
                    }
                }
                SourceResult.Status.ERROR -> Utils.showError(it.errorMessage, binding.root, resources)
                else -> Unit
            }
        })
    }
}