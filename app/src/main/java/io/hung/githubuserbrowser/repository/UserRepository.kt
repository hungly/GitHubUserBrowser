/*
 * MIT License
 *
 * Copyright (c) 2020 Hung Quoc Ly
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package io.hung.githubuserbrowser.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.hung.githubuserbrowser.api.model.SearchResult
import io.hung.githubuserbrowser.api.model.User
import io.hung.githubuserbrowser.data.SourceResult
import io.hung.githubuserbrowser.data.getResultIntoLiveData
import io.hung.githubuserbrowser.data.local.UserLocalDataSource
import io.hung.githubuserbrowser.data.remote.UserRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource
) {

    private val _selectedUser = MutableLiveData<SourceResult<User?>>()
    private val _searchResult = MutableLiveData<SourceResult<SearchResult?>>()

    val selectedUser: LiveData<SourceResult<User?>> = _selectedUser
    val searchResult: LiveData<SourceResult<SearchResult?>> = _searchResult

    fun getUser(userLogin: String, userResult: MutableLiveData<SourceResult<User?>> = _selectedUser, scope: CoroutineScope) {
        getResultIntoLiveData(
            databaseQuery = { localDataSource.getUser(userLogin) },
            networkCall = { remoteDataSource.getUser(userLogin) },
            saveResult = { user -> user?.let { localDataSource.addUser(it) } },
            resultLiveData = userResult,
            waitingDuration = 0,
            scope = scope
        )
    }

    fun searchUser(query: String, page: Int = 1, searchResult: MutableLiveData<SourceResult<SearchResult?>> = _searchResult, scope: CoroutineScope) {
        getResultIntoLiveData(
            databaseQuery = { null },
            networkCall = { remoteDataSource.searchUsers(query, page) },
            saveResult = { },
            resultLiveData = searchResult,
            scope = scope
        )
    }
}