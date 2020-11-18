package io.hung.githubuserbrowser.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@Entity
@JsonClass(generateAdapter = true)
data class User(
     @PrimaryKey val id: Int,
     val login: String,
     val name: String?,
     @Json(name = "avatar_url") val avatarUrl: String,
     val bio: String?,
     val followers: Int?,
     val following: Int?,
     val company: String?,
     val location: String?
) : Serializable