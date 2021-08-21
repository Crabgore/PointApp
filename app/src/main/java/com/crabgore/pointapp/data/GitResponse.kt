package com.crabgore.pointapp.data

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val total_count: Int,
    val incompleteResults: Boolean,
    val items: List<User>
)

data class User(
    val login: String,
    val id: Int,
    val nodeID: String? = null,
    @SerializedName("avatar_url")
    val avatarURL: String? = null,
    val gravatarID: String? = null,
    val url: String? = null,
    val htmlURL: String? = null,
    val followersURL: String? = null,
    val followingURL: String? = null,
    val gistsURL: String? = null,
    val starredURL: String? = null,
    val subscriptionsURL: String? = null,
    val organizationsURL: String? = null,
    val reposURL: String? = null,
    val eventsURL: String? = null,
    val receivedEventsURL: String? = null,
    val type: String? = null,
    val siteAdmin: Boolean? = null
)