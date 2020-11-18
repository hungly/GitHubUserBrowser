package io.hung.githubuserbrowser.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import io.hung.githubuserbrowser.api.model.User
import io.hung.githubuserbrowser.UserComparator
import io.hung.githubuserbrowser.adapter.viewholder.UserVH
import io.hung.githubuserbrowser.UserViewModel

class UserAdapter(private val viewModel: UserViewModel) : RecyclerView.Adapter<UserVH>() {

    private val userList = SortedList(User::class.java, UserComparator(this))

    override fun getItemCount(): Int = userList.size()

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(userList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH = UserVH.create(parent, viewModel)

    fun addUsers(newUsers: List<User>) {
        userList.addAll(newUsers)
    }
}