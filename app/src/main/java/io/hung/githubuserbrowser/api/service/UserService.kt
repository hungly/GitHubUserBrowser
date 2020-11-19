package io.hung.githubuserbrowser.api.service

import io.hung.githubuserbrowser.BuildConfig
import io.hung.githubuserbrowser.api.model.SearchResult
import io.hung.githubuserbrowser.api.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("/users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<User>

    @GET("/search/users")
    suspend fun searchUser(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") userPerPage: Int = BuildConfig.USER_PER_PAGE
    ): Response<SearchResult>
}