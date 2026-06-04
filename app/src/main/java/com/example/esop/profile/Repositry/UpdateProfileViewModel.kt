package com.example.esop.profile.Repositry

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.profile.request.UpadteProfileRequest
import kotlinx.coroutines.launch
import signup.Repositry.SignupRepository
import signup.SignupState
import signup.request.SignupRequest

class UpdateProfileViewModel : ViewModel() {




    private val repo = UpdateProfileRepository()

    var state by mutableStateOf<SignupState>(SignupState.Idle)
        private set

    fun UpdateProfile(context: Context,request: UpadteProfileRequest) {
        viewModelScope.launch {
            state = SignupState.Loading

            val result = repo.UpdateProfile(request)

            state = result.fold(
                onSuccess = {
                    if (it.success) SignupState.Success(it)
                    else SignupState.Error(it.message)
                },
                onFailure = {
                    SignupState.Error(it.message ?: "Error")
                }
            )
        }
    }

}