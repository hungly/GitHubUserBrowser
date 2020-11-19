package io.hung.githubuserbrowser.ui.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.hung.githubuserbrowser.repository.UserRepository
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val selectedUser = userRepository.selectedUser

    fun getUser(userLogin: String) {
        userRepository.getUser(userLogin = userLogin, scope = viewModelScope)
    }
}