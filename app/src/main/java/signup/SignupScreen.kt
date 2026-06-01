package signup

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esop.ProcessGroup.ProcessGroupDropdown
import com.example.esop.ProcessGroup.ProcessGroupViewModel
import com.example.esop.ui.theme.dimens
import signup.request.SignupRequest




import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.esop.FunctionaryDropdown.FunctionaryViewModel
import com.example.esop.OrgnazationDropdown.RoleViewModel
import com.example.esop.district.DistrictViewModel
import com.example.esop.profile.CompleteProfileScreen
import com.example.esop.state.StateViewModel
import com.example.esop.util.CommonDropdown
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = viewModel()
)

{

    val dimens = MaterialTheme.dimens
    val context = LocalContext.current
    val state = viewModel.state

    val scrollState = rememberScrollState()

    // 🔹 ALL FIELDS (UNCHANGED)
    var email by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    var alternateEmail by remember { mutableStateOf("") }

    var aadhaar by remember { mutableStateOf("") }
    var pan by remember { mutableStateOf("") }
    var dl by remember { mutableStateOf("") }

    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    var address by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    val errorMap = remember { mutableStateMapOf<String, String>() }
    val focusMap = remember { mutableMapOf<String, FocusRequester>() }

    // ❌ OLD
    // var processGroup by remember { mutableStateOf("") }

    // ✅ NEW (Dropdown State)
    var processGroupName by remember { mutableStateOf("") }
    var OrganizationName by remember { mutableStateOf("") }
    var FunctionaryName by remember { mutableStateOf("") }
    var processGroupCode by remember { mutableStateOf("") }
    var OrganizationCode by remember { mutableStateOf("") }

    var stateName by remember { mutableStateOf("") }
    var stateCode by remember { mutableStateOf("") }



    var districtname by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female", "Other")
//    var districtcode by remember { mutableStateOf("") }
//    var organization by remember { mutableStateOf("") }
//    var functionary by remember { mutableStateOf("") }

    var country by remember { mutableStateOf("") }
    var stateField by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    var designation by remember { mutableStateOf("") }

    var error by remember { mutableStateOf("") }


// Keyboard change handler
    val dlKeyboardType =
        when (dl.length) {

            // First 2 = State Code Alphabets
            in 0..1 -> KeyboardType.Text

            // Remaining = Numbers
            else -> KeyboardType.Number
        }


// Indian State Codes
    val stateCodes = listOf(
        "AP","AR","AS","BR","CG","CH","DD","DL","DN","GA",
        "GJ","HR","HP","JH","JK","KA","KL","LD","MH","ML",
        "MN","MP","MZ","NL","OD","PB","PY","RJ","SK","TN",
        "TR","TS","UK","UP","WB","AN","LA"
    )


    // ✅ ViewModel for dropdown
    val processGroupViewModel: ProcessGroupViewModel = viewModel()
    val roleViewModel: RoleViewModel = viewModel()
    val functionaryviewModel: FunctionaryViewModel = viewModel()
    val stateviewModel: StateViewModel = viewModel()
    val districtviewModel: DistrictViewModel = viewModel()




    // ✅ API Call
    LaunchedEffect(Unit) {

        processGroupViewModel.fetchProcessGroups()
//        stateviewModel.fetchState()
    }
    LaunchedEffect(errorMap.size) {
        if (errorMap.isNotEmpty()) {
            coroutineScope.launch {
                scrollState.animateScrollTo(0)
            }
        }
    }

    Scaffold(containerColor = Color.White,

        // 🔝 TOP UI SAME
        topBar = {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F7FA))
            ) {

                val screenWidth = maxWidth

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimens.spaceXL),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(dimens.iconXL)
                    )

                    Text(
                        text = "ESOP",
                        fontSize = (screenWidth.value * 0.08).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB)
                    )

                    Text(
                        text = "Create your account",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(dimens.spaceM))
                }
            }
        },

        // 🔘 BUTTON SAME (ONLY 1 LINE CHANGED)
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                if (error.isNotEmpty()) {
                    Text(error, color = Color.Red)
                }

                Button(
                    onClick = {

                        val request = SignupRequest(
                            email,
                            confirmEmail,
                            password,
                            firstName,
                            lastName,
                            alternateEmail,
                            aadhaar,
                            pan,
                            dl,
                            age.toIntOrNull() ?: 0,
                            gender,
                            address,
                            mobile,
                            telephone,
                            processGroupName,   // ✅ IMPORTANT CHANGE
                            OrganizationName,
                            FunctionaryName,
                            country,
                            stateField,
                            district,
                            city,
                            designation
                        )

                        val validationError = SignupValidator.validate(request)

                        if (validationError != null) {
                            error = validationError
                        } else {
                            error = ""
                            viewModel.signup(context, request)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.buttonHeight),
                    shape = RoundedCornerShape(dimens.radiusM),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB)
                    )
                ) {
                    Text("Submit")
                }
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = dimens.screenPaddingHorizontal)
        ) {

            Spacer(modifier = Modifier.height(dimens.spaceM))

            // 🔹 Account
            SectionTitle("Account Details")
            InputField(email, { email = it }, "Email")
            InputField(confirmEmail, { confirmEmail = it }, "Confirm Email")
            InputField(password, { password = it }, "Password", isPassword = true)

            // 🔹 Personal
            SectionTitle("Personal Info")
            InputField(firstName, { firstName = it }, "First Name")
            InputField(lastName, { lastName = it }, "Last Name")
            InputField(alternateEmail, { alternateEmail = it }, "Alternate Email")

            // 🔹 Identity
            SectionTitle("Identity")
//            InputField(aadhaar, { aadhaar = it }, "Aadhaar", KeyboardType.Number)
//            InputField(pan, { pan = it }, "PAN")
//            InputField(dl, { dl = it }, "Driving License")

            InputField(
                aadhaar,
                {
                    if (it.length <= 12 && it.all { char -> char.isDigit() }) {
                        aadhaar = it
                    }
                },
                "Aadhaar",
                KeyboardType.Number
            )

            var pan by remember { mutableStateOf("") }

            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            val keyboardType =
                when (pan.length) {

                    // First 5 = Alphabets
                    in 0..4 -> KeyboardType.Text

                    // Next 4 = Numbers
                    in 5..8 -> KeyboardType.Number

                    // Last 1 = Alphabet
                    9 -> KeyboardType.Text

                    else -> KeyboardType.Text
                }


            InputField(

                pan,

                { input ->

                    val value = input.uppercase()

                    if (value.length <= 10) {

                        val valid = value.withIndex().all { (index, char) ->

                            when (index) {

                                // First 5 letters
                                in 0..4 -> char.isLetter()

                                // Next 4 numbers
                                in 5..8 -> char.isDigit()

                                // Last character letter
                                9 -> char.isLetter()

                                else -> false
                            }
                        }

                        if (valid || value.isEmpty()) {

                            pan = value

                            // Auto close keyboard after complete PAN
                            if (value.length == 10) {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        }
                    }
                },

                "PAN",

                keyboardType
            )
            OutlinedTextField(

                value = dl,

                onValueChange = { input ->

                    val value = input.uppercase()

                    // Max length 16
                    if (value.length <= 16) {

                        val valid = value.withIndex().all { (index, char) ->

                            when (index) {

                                // First 2 = Alphabets
                                0,1 -> char.isLetter()

                                // Remaining = Numbers
                                else -> char.isDigit()
                            }
                        }

                        if (valid || value.isEmpty()) {

                            // Validate state code
                            if (
                                value.length < 2 ||
                                stateCodes.contains(value.take(2))
                            ) {

                                dl = value

                                // Auto dismiss keyboard
                                if (value.length == 16) {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            }
                        }
                    }
                },

                label = {
                    Text("Driving License")
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = dlKeyboardType,
                    capitalization = KeyboardCapitalization.Characters
                ),

                modifier = Modifier.fillMaxWidth()
            )

//            InputField(dl, { dl = it }, "Driving License")




// Indian State Codes



            InputField(

                dl,

                { input ->

                    val value = input.uppercase()

                    // Max Length 16
                    if (value.length <= 16) {

                        val valid = value.withIndex().all { (index, char) ->

                            when (index) {

                                // First 2 characters = Alphabets
                                0,1 -> char.isLetter()

                                // Remaining = Numbers
                                else -> char.isDigit()
                            }
                        }

                        if (valid || value.isEmpty()) {

                            // Validate state code after first 2 chars
                            if (
                                value.length < 2 ||
                                stateCodes.contains(value.take(2))
                            ) {

                                dl = value

                                // Auto close keyboard after full DL
                                if (value.length == 16) {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            }
                        }
                    }
                },

                "Driving License",

                keyboardType
            )


            // 🔹 Basic
            SectionTitle("Basic Info")
            InputField(age, { age = it }, "Age", KeyboardType.Number)
//            InputField(gender, { gender = it }, "Gender")


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                expanded = false

                            }
                        )
                    }
                }
            }

            // 🔹 Contact
            SectionTitle("Contact")
            InputField(address, { address = it }, "Address")
//            InputField(mobile, { mobile = it }, "Mobile", KeyboardType.Number)
            InputField(
                value = mobile,
                onValueChange = {
                    // Allow only 10 digit numbers
                    if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                        mobile = it
                    }
                },
                label = "Mobile",
                keyboardType = KeyboardType.Number
            )
            InputField(telephone, { telephone = it }, "Telephone")

            // 🔹 Work
            SectionTitle("Work Info")
            CommonDropdown(
                list = processGroupViewModel.processGroupList,
                selectedText = processGroupName,
                label = "Process Group",
                itemText = { it.level_short_name ?: "" },
                onItemSelected = { item ->
                    processGroupName = item.level_short_name ?: ""
                    processGroupCode = item.level_admin_cd ?: ""
                    roleViewModel.fetchRoles(processGroupCode) // API call
                }
            )

            CommonDropdown(
                list = roleViewModel.roleList,
                selectedText = OrganizationName,
                label = "Organization",
                itemText = { it.name_of_the_org ?: "" },
                onItemSelected = { item ->
                    OrganizationName = item.name_of_the_org ?: ""
                    OrganizationCode = (item.org_id ?: "")?: ""

                    functionaryviewModel.fetchFunctionaries(OrganizationCode) // API call
                }
            )


            CommonDropdown(
                list = functionaryviewModel.functionaryList,
                selectedText = FunctionaryName,
                label = "Functionary",
                itemText = { it.user_design ?: "" },
                onItemSelected = { item ->
                    FunctionaryName = item.user_design ?: ""
                    OrganizationCode = (item.user_design ?: "")?: ""

                    stateviewModel.fetchState()
                }
            )

            // 🔹 Location
            SectionTitle("Location")
            InputField(country, { country = it }, "Country")

            CommonDropdown(

                list = stateviewModel.stateList,

                selectedText = stateName,

                label = "State",

                itemText = { it.statename },

                onItemSelected = { item ->

//                    stateField = item.statename
                    stateName = item.statename

                    stateCode = item.statecode
                    districtviewModel.fetchDistrict(stateCode)
                }
            )
            CommonDropdown(

                list = districtviewModel.districtList,

                selectedText = districtname,

                label = "District",
                itemText = { it.districtname },

                onItemSelected = { item ->

                    districtname = item.districtname
                    district = item.districtname

//                    district = item.districtcode
//                    districtviewModel.fetchDistrict(stateCode)
                }
            )

            InputField(city, { city = it }, "City")

            SectionTitle("Location")
            InputField(country, { country = it }, "Country")


//            InputField(district, { district = it }, "District")

            // 🔹 Other
            SectionTitle("Other")
            InputField(designation, { designation = it }, "Designation")

            Spacer(modifier = Modifier.height(120.dp))
        }
    }

    // 🔄 STATE HANDLE SAME
    when (state) {

        is SignupState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is SignupState.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate("login") {
                    popUpTo("signup") { inclusive = true }
                }
            }
        }

        is SignupState.Error -> {
            error = state.message
        }

        else -> {}


    }
    fun validateFields(): Boolean {

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
}


@Preview(showBackground = true)
@Composable
fun SignupScreen() {

    SignupScreen(
        navController = rememberNavController()
    )
}

