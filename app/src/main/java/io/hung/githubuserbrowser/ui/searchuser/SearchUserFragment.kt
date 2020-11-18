package io.hung.githubuserbrowser.ui.searchuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.hung.githubuserbrowser.R
import io.hung.githubuserbrowser.SearchUserViewModel
import io.hung.githubuserbrowser.UserDecoration
import io.hung.githubuserbrowser.adapter.UserAdapter
import io.hung.githubuserbrowser.databinding.SearchUserFragmentBinding

class SearchUserFragment : Fragment() {

    private lateinit var binding: SearchUserFragmentBinding
    private val adapter by lazy { UserAdapter(viewModel) }
    private val viewModel: SearchUserViewModel by activityViewModels()

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

    override fun onResume() {
        super.onResume()

        viewModel.updateUsers()
    }

    private fun setupObservers() {
        viewModel.selectedUser.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            findNavController().navigate(SearchUserFragmentDirections.actionSearchUserFragmentToUserDetailFragment(it))
            viewModel.doneNavigateToDetail()
        })

        viewModel.users.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            adapter.updateUsers(it)
        })
    }

    private fun setupViews() {
        binding.rvSearchUser.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSearchUser.adapter = adapter
        binding.rvSearchUser.addItemDecoration(UserDecoration(resources.getDimensionPixelSize(R.dimen.user_item_padding)))
    }
}