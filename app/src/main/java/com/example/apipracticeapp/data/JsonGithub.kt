package com.example.apipracticeapp.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class JsonGithub(
    val items: List<Content>
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "id")
    val id: Int,
    val name: String,
    @Json(name = "owner")
    val owner: Owner,
    @Json(name = "language")
    val language: String?,
    @Json(name = "stargazers_count")
    val stargazersCount: Long,
    @Json(name = "watchers_count")
    val watchersCount: Long,
    @Json(name = "forks_count")
    val forksCount: Long,
    @Json(name = "open_issues_count")
    val openIssuesCount: Long,
)

@JsonClass(generateAdapter = true)
data class Owner(
    @Json(name = "avatar_url")
    val avatarUrl: String
)

// SafeArgs用
@Parcelize
data class Item(
    val name: String,
    val ownerIconUrl: String,
    val language: String?,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable

