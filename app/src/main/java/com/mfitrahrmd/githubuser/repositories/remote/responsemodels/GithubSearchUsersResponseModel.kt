package com.mfitrahrmd.githubuser.repositories.remote.responsemodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mfitrahrmd.githubuser.entities.network.NetworkUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubSearchUsersResponseModel(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val items: List<NetworkUser>
) : Parcelable