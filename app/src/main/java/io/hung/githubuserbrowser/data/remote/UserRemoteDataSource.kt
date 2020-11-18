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

package io.hung.githubuserbrowser.data.remote

import com.squareup.moshi.JsonAdapter
import io.hung.githubuserbrowser.api.model.ApiError
import io.hung.githubuserbrowser.api.service.UserService
import io.hung.githubuserbrowser.data.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    adapter: JsonAdapter<ApiError>,
    private val userService: UserService
) : BaseDataSource(adapter) {

    suspend fun getUser(userLogin: String) = getResult { userService.getUser(userLogin) }

    suspend fun searchUsers(searchQuery: String, page: Int) = getResult { userService.searchUser(searchQuery, page) }
}