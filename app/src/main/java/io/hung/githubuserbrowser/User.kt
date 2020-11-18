package io.hung.githubuserbrowser

import java.io.Serializable

data class User(
     val id: Int,
     val name: String,
     val nick: String
) : Serializable