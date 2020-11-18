package io.hung.githubuserbrowser.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.hung.githubuserbrowser.api.model.User

@JsonClass(generateAdapter = true)
data class SearchResult(
    @Json(name = "total_count") val totalCount: Int,
    val items: List<User>
)