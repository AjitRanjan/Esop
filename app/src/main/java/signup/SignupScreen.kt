package signup

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.esop.FunctionaryDropdown.FunctionaryViewModel
import com.example.esop.OrgnazationDropdown.RoleViewModel
import com.example.esop.district.DistrictViewModel
import com.example.esop.network.Resource
import com.example.esop.profile.CompleteProfileScreen
import com.example.esop.state.StateViewModel
import com.example.esop.util.Base64Utils
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
    var userid by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usertype by remember { mutableStateOf("") }

     var mobile by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    val errorMap = remember { mutableStateMapOf<String, String>() }
    val focusMap = remember { mutableMapOf<String, FocusRequester>() }
    var isOrganizationVisible by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    // ❌ OLD
    // var processGroup by remember { mutableStateOf("") }

    // ✅ NEW (Dropdown State)
    var processGroupName by remember { mutableStateOf("") }
    var OrganizationName by remember { mutableStateOf("") }
//    var FunctionaryName by remember { mutableStateOf("") }
    var processGroupCode by remember { mutableStateOf("") }
    var OrganizationCode by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val usertypedesc = listOf("Operation", "Finance")
    var departmentType by remember { mutableStateOf("") }
    val signupState = viewModel.state
    var error by remember { mutableStateOf("") }
    var isNavigated by remember {
        mutableStateOf(false)
    }

// Keyboard change handler



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
                            "",
                            password,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            mobile,
                            "",
                            processGroupName,
                            OrganizationName,
                            "",
                            "India",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            departmentType,
                            usertype,
                            processGroupName+mobile,
                            "",
                            processGroupCode,
                            OrganizationCode
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
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            )
            {
                OutlinedTextField(
                    value = departmentType,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Please Choose Your Department Type")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    usertypedesc.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option)
                            },
                            onClick = {
                                departmentType = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            CommonDropdown(
                list = processGroupViewModel.processGroupList,
                selectedText = processGroupName,
                label = "Process Group",
                itemText = { it.level_short_name ?: "" },
                onItemSelected = { item ->
                    processGroupName = item.level_short_name ?: ""
                    processGroupCode = item.level_admin_cd ?: ""

                    roleViewModel.clearRoles()

                    if (processGroupName.equals("OTHERS", ignoreCase = true)) {
                        OrganizationName = "OTHERS"
                        OrganizationCode = 0.toString()
                        processGroupCode = 0.toString()
                        usertype="External"
                        isOrganizationVisible = false
                    } else {
                        OrganizationName = ""
                        OrganizationCode = ""
                        isOrganizationVisible = true
                        usertype="Internal"
                        roleViewModel.fetchRoles(processGroupCode)
                    }
                }
            )

            if (isOrganizationVisible) {
                CommonDropdown(
                    list = roleViewModel.roleList,
                    selectedText = OrganizationName,
                    label = "Organization",
                    itemText = { it.name_of_the_org ?: "" },
                    onItemSelected = { item ->

                        if (OrganizationName.equals("OTHERS", ignoreCase = true)) {
                            OrganizationName = "OTHERS"
                            OrganizationCode = 0.toString()
                            usertype="External"

                        } else {
                            OrganizationName = item.name_of_the_org ?: ""
                            OrganizationCode = item.org_id ?: ""
                            usertype="Internal"
                        }
                        functionaryviewModel.fetchFunctionaries(OrganizationCode)
                    }
                )
            }
//            InputField(userid, { userid = it }, "User Id", enabled = false)
//            InputField(userid, { userid = it }, "User Id")
            InputField(email, { email = it }, "Email")
            OutlinedTextField(

                value = password,

                onValueChange = {
                    password = it
                },

                label = {
                    Text("Password")
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(dimens.radiusM),

                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

                trailingIcon = {

                    IconButton(
                        onClick = {

                            passwordVisible =
                                !passwordVisible
                        }
                    ) {

                        Icon(
                            imageVector =
                                if (passwordVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,

                            contentDescription = null,

                            tint = Color(0xFF2563EB)
                        )
                    }
                }
            )
//            InputField(password, { password = it }, "Password", isPassword = true)
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
        }

            Spacer(modifier = Modifier.height(120.dp))


        }


    LaunchedEffect(signupState) {
        when (signupState) {
            is SignupState.Success -> {




                Toast.makeText(
                    context,
                    signupState.response.message,
                    Toast.LENGTH_LONG
                ).show()



                if (!isNavigated) {
                    isNavigated = true

                    navController.navigate("login") {
                        popUpTo("signup") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }

            is SignupState.Error -> {
                Toast.makeText(
                    context,
                    signupState.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> Unit
        }
    }


    fun validateFields(): Boolean {

        errorMap.clear()
        if (departmentType.isBlank()) errorMap["dp"] = "Select Department Type"
        if (processGroupName.isBlank()) errorMap["pg"] = "Select Process Group"
        if (OrganizationName.isBlank()) errorMap["org"] = "Select Organization"

//        if (userid.isBlank()) errorMap["userid"] = "Enter User Id"
        if (email.isBlank()) errorMap["email"] = "Enter Email"

        if (password.length < 6) errorMap["password"] = "Min 6 char"


        if (mobile.length != 10) errorMap["mobile"] = "Invalid Mobile"



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

