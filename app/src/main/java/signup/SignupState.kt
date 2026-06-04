package signup
sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    data class Success(val response: SignupResponse) : SignupState()
    data class Error(val message: String) : SignupState()
}
//sealed class SignupState {
//    object Idle : SignupState()
//    object Loading : SignupState()
//    data class Success(val data: SignupResponse) : SignupState()
//    data class Error(val message: String) : SignupState()
//}