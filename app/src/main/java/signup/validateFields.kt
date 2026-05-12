package signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester

fun validateFields(
    email: String,
    confirmEmail: String,
    password: String,
    firstName: String,
    lastName: String,
    aadhaar: String,
    pan: String,
    dl: String,
    age: String,
    gender: String,
    address: String,
    mobile: String,
    processGroupName: String,
    OrganizationName: String,
    FunctionaryName: String,
    country: String,
    stateField: String,
    district: String,
    city: String,
    designation: String,
    errorMap: MutableMap<String, String>
): Boolean {

    errorMap.clear()

    if (email.isBlank()) errorMap["email"] = "Enter Email"
    if (confirmEmail != email) errorMap["confirmEmail"] = "Email mismatch"
    if (password.length < 6) errorMap["password"] = "Min 6 char"

    if (firstName.isBlank()) errorMap["firstName"] = "Enter First Name"
    if (lastName.isBlank()) errorMap["lastName"] = "Enter Last Name"

    if (aadhaar.length != 12) errorMap["aadhaar"] = "Invalid Aadhaar"
    if (pan.length != 10) errorMap["pan"] = "Invalid PAN"
    if (dl.isBlank()) errorMap["dl"] = "Enter DL"

    if (age.isBlank()) errorMap["age"] = "Enter Age"
    if (gender.isBlank()) errorMap["gender"] = "Select Gender"

    if (address.isBlank()) errorMap["address"] = "Enter Address"
    if (mobile.length != 10) errorMap["mobile"] = "Invalid Mobile"

    if (processGroupName.isBlank()) errorMap["pg"] = "Select Process Group"
    if (OrganizationName.isBlank()) errorMap["org"] = "Select Organization"
    if (FunctionaryName.isBlank()) errorMap["func"] = "Select Functionary"

    if (country.isBlank()) errorMap["country"] = "Enter Country"
    if (stateField.isBlank()) errorMap["state"] = "Enter State"
    if (district.isBlank()) errorMap["district"] = "Enter District"
    if (city.isBlank()) errorMap["city"] = "Enter City"

    if (designation.isBlank()) errorMap["designation"] = "Enter Designation"

    return errorMap.isEmpty()
}

//@Composable
//fun validateFields(): Boolean {
//// 🔹 ALL FIELDS (UNCHANGED)
//    var email by remember { mutableStateOf("") }
//    var confirmEmail by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//
//    var alternateEmail by remember { mutableStateOf("") }
//
//    var aadhaar by remember { mutableStateOf("") }
//    var pan by remember { mutableStateOf("") }
//    var dl by remember { mutableStateOf("") }
//
//    var age by remember { mutableStateOf("") }
//    var gender by remember { mutableStateOf("") }
//
//    var address by remember { mutableStateOf("") }
//    var mobile by remember { mutableStateOf("") }
//    var telephone by remember { mutableStateOf("") }
//
//
//    // ✅ NEW (Dropdown State)
//    var processGroupName by remember { mutableStateOf("") }
//    var OrganizationName by remember { mutableStateOf("") }
//    var FunctionaryName by remember { mutableStateOf("") }
//
//
//    var country by remember { mutableStateOf("") }
//    var stateField by remember { mutableStateOf("") }
//    var district by remember { mutableStateOf("") }
//    var city by remember { mutableStateOf("") }
//
//    var designation by remember { mutableStateOf("") }
//
////    val coroutineScope = rememberCoroutineScope()
//
//    val errorMap = remember { mutableStateMapOf<String, String>() }
//    val focusMap = remember { mutableMapOf<String, FocusRequester>() }
//    errorMap.clear()
//
//    if (email.isBlank()) errorMap["email"] = "Enter Email"
//    if (confirmEmail != email) errorMap["confirmEmail"] = "Email mismatch"
//    if (password.length < 6) errorMap["password"] = "Min 6 char"
//
//    if (firstName.isBlank()) errorMap["firstName"] = "Enter First Name"
//    if (lastName.isBlank()) errorMap["lastName"] = "Enter Last Name"
//
//    if (aadhaar.length != 12) errorMap["aadhaar"] = "Invalid Aadhaar"
//    if (pan.length != 10) errorMap["pan"] = "Invalid PAN"
//    if (dl.isBlank()) errorMap["dl"] = "Enter DL"
//
//    if (age.isBlank()) errorMap["age"] = "Enter Age"
//    if (gender.isBlank()) errorMap["gender"] = "Select Gender"
//
//    if (address.isBlank()) errorMap["address"] = "Enter Address"
//    if (mobile.length != 10) errorMap["mobile"] = "Invalid Mobile"
//
//    if (processGroupName.isBlank()) errorMap["pg"] = "Select Process Group"
//    if (OrganizationName.isBlank()) errorMap["org"] = "Select Organization"
//    if (FunctionaryName.isBlank()) errorMap["func"] = "Select Functionary"
//
//    if (country.isBlank()) errorMap["country"] = "Enter Country"
//    if (stateField.isBlank()) errorMap["state"] = "Enter State"
//    if (district.isBlank()) errorMap["district"] = "Enter District"
//    if (city.isBlank()) errorMap["city"] = "Enter City"
//
//    if (designation.isBlank()) errorMap["designation"] = "Enter Designation"
//
//    return errorMap.isEmpty()
//}