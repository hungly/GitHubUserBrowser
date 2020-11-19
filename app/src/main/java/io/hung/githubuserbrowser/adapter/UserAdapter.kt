package io.hung.githubuserbrowser.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.hung.githubuserbrowser.UserComparator
import io.hung.githubuserbrowser.UserViewModel
import io.hung.githubuserbrowser.adapter.viewholder.UserVH
import io.hung.githubuserbrowser.api.model.User

class UserAdapter(
    private val imageRequestOptions: RequestOptions,
    private val transitionOptions: DrawableTransitionOptions,
    private val viewModel: UserViewModel
) : RecyclerView.Adapter<UserVH>() {

    private val userList = SortedList(User::class.java, UserComparator(this))

    override fun getItemCount(): Int = userList.size()

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(userList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH = UserVH.create(parent, imageRequestOptions, transitionOptions, viewModel)

    fun addUsers(newUsers: List<User>) {
        userList.addAll(newUsers)
    }
}