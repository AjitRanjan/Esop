package com.example.esop.token

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.OrgnazationDropdown.RoleItem
import com.example.esop.district.DistrictItem
import com.example.esop.login.AuthRepository
import com.example.esop.login.LoginRequest
import com.example.esop.login.LoginResponse
import com.example.esop.network.Resource
import com.example.esop.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TokenViewModel : ViewModel() {

    private val repository = AuthTokenRepository()

    private val _tokenState =
        MutableStateFlow<Resource<GetToken>?>(null)
    val tokenState: StateFlow<Resource<GetToken>?> =
        _tokenState

    fun getToke(appVersion: String, imeiNo: String) {
        viewModelScope.launch {

            _tokenState.value = Resource.Loading()

            val request = TokenRequest(
                appVersion = appVersion,
                imeiNo = imeiNo
            )

            _tokenState.value = repository.getToken(request)
        }
    }
}
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isInternetAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}
