package com.vj.navigationcomponent.model

import com.vj.navigationcomponent.network.GithubEndpoint
import javax.inject.Inject

class GitRepository @Inject constructor(private val endpoint: GithubEndpoint) {
    suspend fun callUserData() = endpoint.requestUserData()
    suspend fun callUserDetailsData(userName: String) = endpoint.requestUserDetailsData(userName)
}