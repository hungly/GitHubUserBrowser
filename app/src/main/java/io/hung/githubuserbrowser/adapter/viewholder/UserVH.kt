package io.hung.githubuserbrowser.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.hung.githubuserbrowser.User
import io.hung.githubuserbrowser.databinding.UserItemBinding
import io.hung.githubuserbrowser.UserViewModel

class UserVH(private val binding: UserItemBinding, viewModel: UserViewModel) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.viewModel = viewModel
    }

    fun bind(user: User) {
        binding.user = user
        binding.executePendingBindings()
    }

    companion object {

        fun create(parent: ViewGroup, viewModel: UserViewModel) = UserVH(
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel
        )
    }
}