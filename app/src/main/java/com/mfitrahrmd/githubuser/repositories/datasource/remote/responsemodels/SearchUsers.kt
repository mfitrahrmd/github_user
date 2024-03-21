package com.mfitrahrmd.githubuser.repositories.datasource.remote.responsemodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mfitrahrmd.githubuser.entities.network.SourceUser
import kotlinx.parcelize.Parcelize

@Parcelize
class SearchUsers(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val items: List<SourceUser>
) : Parcelable