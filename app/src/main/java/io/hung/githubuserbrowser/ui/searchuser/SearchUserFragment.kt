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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.hung.githubuserbrowser.*
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

        viewModel.users.observe(viewLifecycleOwner, {
            binding.tvEmpty.visibility = View.GONE
            binding.tvNotFound.visibility = View.GONE

            when (it.status) {
                SourceResult.Status.SUCCESS -> {
                    it.data?.let { result ->
                        viewModel.updateAvailablePage(result.totalCount / BuildConfig.USER_PER_PAGE)
                        if (viewModel.getCurrentPage() == 1 && result.items.isEmpty()) binding.tvNotFound.visibility = View.VISIBLE

                        if (viewModel.getCurrentPage() == 1) {
                            adapter.newSearchResults(result.items)
                        } else {
                            adapter.addSearchResults(result.items)
                        }
                    }
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

        binding.rvSearchUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = adapter.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                val endHasBeenReached = lastVisible + 5 >= totalItemCount
                if (viewModel.hasNextPage() && totalItemCount > 0 && endHasBeenReached) {
                    viewModel.loadNextPage()
                }
            }
        })
    }
}