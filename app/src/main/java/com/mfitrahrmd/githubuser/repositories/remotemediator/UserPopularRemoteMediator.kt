package com.mfitrahrmd.githubuser.repositories.remotemediator

import com.mfitrahrmd.githubuser.base.BaseRemoteMediator
import com.mfitrahrmd.githubuser.entities.db.DBPopularUser
import com.mfitrahrmd.githubuser.entities.network.NetworkUser
import com.mfitrahrmd.githubuser.repositories.local.dao.PopularUserDao
import com.mfitrahrmd.githubuser.repositories.remote.services.UserService

class UserPopularRemoteMediator(
    private val _location: String,
    private val _userService: UserService,
    private val _popularUserDao: PopularUserDao
) : BaseRemoteMediator<DBPopularUser, NetworkUser>(_popularUserDao, { page, pageSize ->
    val res = _userService.searchUsers("location:$_location", page.toString(), pageSize.toString())
    val users = res.body()?.items
    users ?: emptyList()
})