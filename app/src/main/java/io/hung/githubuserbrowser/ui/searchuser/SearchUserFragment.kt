package io.hung.githubuserbrowser.ui.searchuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.hung.githubuserbrowser.R
import io.hung.githubuserbrowser.UserDecoration
import io.hung.githubuserbrowser.UserViewModel
import io.hung.githubuserbrowser.Utils
import io.hung.githubuserbrowser.adapter.UserAdapter
import io.hung.githubuserbrowser.data.SourceResult
import io.hung.githubuserbrowser.databinding.SearchUserFragmentBinding
import io.hung.githubuserbrowser.di.Injectable
import io.hung.githubuserbrowser.di.injectViewModel
import javax.inject.Inject

class SearchUserFragment : Fragment(), Injectable {

    @Inject
    lateinit var imageRequestOptions: RequestOptions

    @Inject
    lateinit var transitionOptions: DrawableTransitionOptions

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter by lazy { UserAdapter(imageRequestOptions, transitionOptions, viewModel) }
    private val viewModel: UserViewModel by lazy {
        injectViewModel(viewModelFactory)
    }

    private lateinit var binding: SearchUserFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchUserFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupViews()
    }

    private fun setupObservers() {
        viewModel.selectedUser.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            findNavController().navigate(SearchUserFragmentDirections.actionSearchUserFragmentToUserDetailFragment(it.login))
            viewModel.doneNavigateToDetail()
        })

        viewModel.users.observe(viewLifecycleOwner, Observer {
            if (it.data == null) return@Observer

            binding.tvEmpty.visibility = View.GONE

            when (it.status) {
                SourceResult.Status.SUCCESS -> {
                    binding.tvNotFound.visibility = if (viewModel.getCurrentPage() == 1 && it.data.items.isEmpty()) View.VISIBLE
                    else View.GONE

                    adapter.newSearchResults(it.data.items)

                    if (adapter.itemCount <= 1) binding.rvSearchUser.postDelayed({ binding.rvSearchUser.scrollToPosition(0) }, 500)
                }
                SourceResult.Status.ERROR -> Utils.showError(it.errorMessage, binding.root, resources)
                else -> Unit
            }
        })
    }

    private fun setupViews() {
        binding.rvSearchUser.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSearchUser.adapter = adapter
        binding.rvSearchUser.addItemDecoration(UserDecoration(resources.getDimensionPixelSize(R.dimen.user_item_padding)))
    }
}