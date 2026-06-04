package com.example.esop.profile.Repositry

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.profile.UpdateState
import com.example.esop.profile.request.UpadteProfileRequest
import kotlinx.coroutines.launch


class UpdateProfileViewModel : ViewModel() {

    private val repo = UpdateProfileRepository()

    var Updatestate by mutableStateOf<UpdateState>(UpdateState.Idle)
        private set

    fun UpdateProfile(
        context: Context,
        request: UpadteProfileRequest
    ) {
        viewModelScope.launch {

            Updatestate = UpdateState.Loading

            val result = repo.UpdateProfile(request)

            Updatestate = result.fold(
                onSuccess = { response ->

                    if (response.responseDesc.equals("Details Updated Successfully.", ignoreCase = true)) {
                        UpdateState.Success(response)
                    } else {
                        UpdateState.Error(response.responseDesc)
                    }
                },
                onFailure = { exception ->
                    UpdateState.Error(
                        exception.message ?: "Something went wrong"
                    )
                }
            )
        }
    }
}
//class UpdateProfileViewModel : ViewModel() {
//
//
//
//
//    private val repo = UpdateProfileRepository()
//
//    var Updatestate by mutableStateOf<UpdateState>(UpdateState.Idle)
//        private set
//
//    fun UpdateProfile(context: Context,request: UpadteProfileRequest) {
//        viewModelScope.launch {
//            Updatestate = UpdateState.Loading
//
//            val result = repo.UpdateProfile(request)
//
//            Updatestate = result.fold(
//                onSuccess = {
//                    if (it.responseDesc)
//                        UpdateState.Success(it)
//                    else
//                        UpdateState.Error(it.responseDesc)
////                    if (it.success) UpdateState.Success(it)
////                    else UpdateState.Error(it.message)
//                },
//                onFailure = {
//                    UpdateState.Error(it.message ?: "Error")
//                }
//            )
//        }
//    }
//
//}