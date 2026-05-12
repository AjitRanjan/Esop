package signup

import signup.request.SignupRequest

object SignupValidator {

    fun validate(request: SignupRequest): String? {

        return when {

            request.email.isEmpty() -> "Email required"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(request.email).matches() -> "Invalid email"

            request.confirmEmail != request.email -> "Email not matched"

            request.password.length < 6 -> "Password must be 6+ chars"

            request.firstName.isEmpty() -> "First name required"
            request.lastName.isEmpty() -> "Last name required"

            request.mobile.length != 10 -> "Mobile must be 10 digits"

            request.aadhaarId.length != 12 -> "Aadhaar must be 12 digits"

            request.panNo.isEmpty() -> "PAN required"

            request.age <= 0 -> "Invalid age"

            request.gender.isEmpty() -> "Select gender"

            request.address.isEmpty() -> "Address required"

            request.designation.isEmpty() -> "Designation required"

            else -> null
        }
    }
}