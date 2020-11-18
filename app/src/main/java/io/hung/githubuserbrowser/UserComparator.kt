package io.hung.githubuserbrowser

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedListAdapterCallback
import io.hung.githubuserbrowser.api.model.User

class UserComparator(adapter: RecyclerView.Adapter<*>?) : SortedListAdapterCallback<User>(adapter) {

    override fun areContentsTheSame(oldItem: User?, newItem: User?): Boolean =
        oldItem?.login?.equals(newItem?.login, true) ?: false

    override fun areItemsTheSame(item1: User?, item2: User?): Boolean =
        item1?.id == item2?.id

    override fun compare(o1: User?, o2: User?): Int =
        o1?.name?.compareTo(o2?.name ?: "") ?: 0
}