package io.hung.githubuserbrowser.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.hung.githubuserbrowser.GlideApp
import io.hung.githubuserbrowser.UserViewModel
import io.hung.githubuserbrowser.api.model.User
import io.hung.githubuserbrowser.databinding.UserItemBinding

class UserVH(
    private val imageRequestOptions: RequestOptions,
    private val transitionOptions: DrawableTransitionOptions,
    private val binding: UserItemBinding,
    viewModel: UserViewModel
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.viewModel = viewModel
    }

    fun bind(user: User) {
        binding.user = user
        binding.executePendingBindings()

        GlideApp.with(binding.root)
            .load(user.avatarUrl)
            .apply(imageRequestOptions)
            .transition(transitionOptions)
            .into(binding.ivAvatar)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            imageRequestOptions: RequestOptions,
            transitionOptions: DrawableTransitionOptions,
            viewModel: UserViewModel
        ) = UserVH(
            imageRequestOptions,
            transitionOptions,
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel
        )
    }
}