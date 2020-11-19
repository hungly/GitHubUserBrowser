package io.hung.githubuserbrowser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.hung.githubuserbrowser.api.model.User
import io.hung.githubuserbrowser.repository.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _selectedUser = MutableLiveData<User>()

    val selectedUser: LiveData<User?> = _selectedUser
    val users = userRepository.searchResult

    private var currentQuery = ""
    private var currentPage = 1
    private var availablePage = 1

    fun doneNavigateToDetail() {
        _selectedUser.postValue(null)
    }

    fun getCurrentPage() = currentPage

    fun hasNextPage() = currentPage < availablePage

    fun loadNextPage() {
        currentPage++
        if (hasQuery() && hasNextPage()) userRepository.searchUser(query = currentQuery, page = currentPage, scope = viewModelScope)
    }

    fun navigateToDetail(user: User) {
        _selectedUser.postValue(user)
    }

    fun searchUsers(query: String) {
        currentQuery = query
        currentPage = 1
        availablePage = 1
        if (hasQuery()) userRepository.searchUser(query = currentQuery, page = currentPage, scope = viewModelScope)
    }

    fun updateAvailablePage(newAvailablePage: Int) {
        availablePage = newAvailablePage
    }

    private fun hasQuery() = currentQuery.isNotBlank() && currentQuery.isNotEmpty()
}