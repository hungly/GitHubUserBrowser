package io.hung.githubuserbrowser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class UserViewModel @Inject constructor() : ViewModel() {

    val users = MutableLiveData<List<User>?>()

    private val _selectedUser = MutableLiveData<User>()

    val selectedUser: LiveData<User?> = _selectedUser

    fun doneNavigateToDetail() {
        _selectedUser.postValue(null)
    }

    fun navigateToDetail(user: User) {
        _selectedUser.postValue(user)
    }

    fun updateUsers() {
        users.postValue((0..25).map { User(it, "Name $it", "nick$it") })
    }
}