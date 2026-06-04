package com.example.esop.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _profileState =
        MutableStateFlow<Resource<ProfileResponse>?>(null)

    val profileState =
        _profileState.asStateFlow()

    fun getProfile(
//        token: String,

        loginId: String,
        email: String,
        appVersion: String
    ) {

        viewModelScope.launch {

            _profileState.value = Resource.Loading()

            val request = ProfileRequest(
                appVersion = appVersion,
                loginId = loginId,
                email = email
            )

            _profileState.value =
                repository.getProfile(
//                    token = token,
                    request = request
                )
        }
    }
}