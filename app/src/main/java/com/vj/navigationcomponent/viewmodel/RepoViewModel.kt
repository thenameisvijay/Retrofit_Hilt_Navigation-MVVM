package com.vj.navigationcomponent.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vj.navigationcomponent.model.GitRepository
import com.vj.navigationcomponent.model.UserDetailsResponse
import com.vj.navigationcomponent.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(private val gitRepository: GitRepository) : ViewModel() {

    var userDetails: MutableLiveData<UserDetailsResponse> = MutableLiveData<UserDetailsResponse>()

    fun getRepoList() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = gitRepository.callUserData()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getUserDetails(userName: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = gitRepository.callUserDetailsData(userName)))

        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}