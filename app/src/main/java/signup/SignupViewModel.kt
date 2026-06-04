package signup

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
import kotlinx.coroutines.launch
import signup.Repositry.SignupRepository
import signup.request.SignupRequest

class SignupViewModel : ViewModel() {




    private val repo = SignupRepository()

    var state by mutableStateOf<SignupState>(SignupState.Idle)
        private set

    fun signup(context: Context,request: SignupRequest) {
        viewModelScope.launch {
            state = SignupState.Loading

            val result = repo.signup(request)

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

//    private val repo = SignupRepository()
//
//    var state by mutableStateOf<SignupState>(SignupState.Idle)
//        private set
//
//    fun signup(context: Context, request: SignupRequest) {
//
//
//
//        viewModelScope.launch {
//            state = SignupState.Loading
//
//            val result = repo.signup(request)
//
//            state = result.fold(
//                onSuccess = {
//                    if (it.success) SignupState.Success(it)
//                    else SignupState.Error(it.message)
//                },
//                onFailure = {
//                    SignupState.Error(it.message ?: "Error")
//                }
//            )
//        }
//    }
//
//
//}
//
//@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
//fun Context.isInternetAvailable(): Boolean {
//    val connectivityManager =
//        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    val network = connectivityManager.activeNetwork ?: return false
//    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
//
//    return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
//            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
//}