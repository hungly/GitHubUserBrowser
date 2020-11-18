package io.hung.githubuserbrowser.ui.searchuser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.hung.githubuserbrowser.User

class SearchUserViewModel : ViewModel() {

    val users = MutableLiveData<List<User>?>()

    fun updateUsers() {
        users.postValue((0..25).map { User(it, "Name $it", "nick$it") })
    }

    fun navigateToDetail(user: User) {
        Log.d("HUNGLY", "${user.id}")
    }
}