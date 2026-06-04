package com.example.esop.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esop.FunctionaryDropdown.FunctionaryViewModel
import com.example.esop.OrgnazationDropdown.RoleViewModel
import com.example.esop.ProcessGroup.ProcessGroupViewModel
import com.example.esop.district.DistrictViewModel
import com.example.esop.state.StateViewModel
import com.example.esop.ui.theme.dimens
import com.example.esop.util.CommonDropdown
import kotlinx.coroutines.launch
import signup.InputField
import signup.SectionTitle
import signup.SignupState

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.asImageBitmap

import coil.compose.rememberAsyncImagePainter
import com.example.esop.network.AppPreferences
import com.example.esop.network.Resource

import com.example.esop.profile.Repositry.UpdateProfileViewModel
import com.example.esop.profile.request.UpadteProfileRequest
import com.example.esop.util.Base64Utils
import com.example.esop.util.ImeiUtils

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CompleteProfileScreen(
    navController: NavController,
    viewModel: CompletePofileScreenViewModel = viewModel(),
    updateModel: UpdateProfileViewModel = viewModel(),
    updateProfileModel: UpdateProfileViewModel
) {

    lateinit var appPrefs: AppPreferences
    val dimens = MaterialTheme.dimens
    val state = viewModel.state
    val UpdateUI = updateModel.Updatestate
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var loginId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var usertype by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    var alternateEmail by remember { mutableStateOf("") }

    var dl by remember { mutableStateOf("") }

    var pincode by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    var address by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    val errorMap = remember { mutableStateMapOf<String, String>() }

    var processGroupName by remember { mutableStateOf("") }
    var OrganizationName by remember { mutableStateOf("") }
    var FunctionaryName by remember { mutableStateOf("") }

    var processGroupCode by remember { mutableStateOf("") }
    var OrganizationCode by remember { mutableStateOf("") }

    var stateName by remember { mutableStateOf("") }
    var stateCode by remember { mutableStateOf("") }

    var districtname by remember { mutableStateOf("") }
    var districtCode by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Other")

    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    var designation by remember { mutableStateOf("") }

    var error by remember { mutableStateOf("") }
    var Token by remember { mutableStateOf("") }

    val processGroupViewModel: ProcessGroupViewModel = viewModel()
    val roleViewModel: RoleViewModel = viewModel()
    val functionaryviewModel: FunctionaryViewModel = viewModel()
    val stateviewModel: StateViewModel = viewModel()
    val districtviewModel: DistrictViewModel = viewModel()


    var profileBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }


    val deviceId = ImeiUtils.getAndroidId(context)


    var isNavigated by remember {
        mutableStateOf(false)
    }

    val versionName = remember {
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName
    }
    appPrefs = AppPreferences(context)
    val authToken by appPrefs.authToken.collectAsState(initial = null)
    val userEmail by appPrefs.userEmail.collectAsState(initial = null)
    val userMobile by appPrefs.mobile.collectAsState(initial = null)
    val userprocessGroup by appPrefs.processGroup.collectAsState(initial = null)
    val userorganization by appPrefs.organization.collectAsState(initial = null)
    val organization_id by appPrefs.processGroupId.collectAsState(initial = null)
    val process_groupId by appPrefs.organizationId.collectAsState(initial = null)
    val userloginId by appPrefs.loginId.collectAsState(initial = null)
    val userusertype by appPrefs.usertype.collectAsState(initial = null)
       mobile=userMobile.toString()

       loginId=userloginId.toString()
    email=userEmail.toString()
    usertype=userusertype.toString()






    LaunchedEffect(UpdateUI) {
        when (UpdateUI) {
            is UpdateState.Success -> {




                Toast.makeText(
                   context,
                    UpdateUI.response.responseDesc,
                    Toast.LENGTH_LONG
                ).show()



                if (!isNavigated) {
                    isNavigated = true

                    navController.navigate("CompleteProfileScreen") {
                        popUpTo("welcome") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }

            is UpdateState.Error -> {
                Toast.makeText(
                    context,
                    UpdateUI.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> Unit
        }
    }





    // ================= IMAGE =================

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    Log.d("authToken", authToken.toString())
    val profileViewModel: ProfileViewModel = viewModel()

    authToken?.let { tokenData ->

        profileViewModel.getProfile(
            token = authToken.toString(),
//                token = "RtqSw0gNXSxGSbtUzN/8Qg==",
            appVersion =versionName.toString(),
            loginId = "2532003643",
            email = userEmail.toString()

        )
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri != null) {
                imageUri = uri
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap: Bitmap? ->

            bitmap?.let {

                val path = MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    it,
                    "Profile",
                    null
                )

                imageUri = Uri.parse(path)
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                cameraLauncher.launch(null)
            }
        }

    // ================= API =================

    LaunchedEffect(Unit) {
        processGroupViewModel.fetchProcessGroups()
    }

    LaunchedEffect(errorMap.size) {

        if (errorMap.isNotEmpty()) {

            coroutineScope.launch {
                scrollState.animateScrollTo(0)
            }
        }
    }

    // ================= UI =================

    Scaffold(

        containerColor = Color.White,

        // ================= TOPBAR =================

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Complete Profile",
                        fontWeight = FontWeight.SemiBold
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        // ================= BOTTOM BUTTON =================

        bottomBar = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                if (error.isNotEmpty()) {

                    Text(
                        text = error,
                        color = Color.Red
                    )
                }

                Button(
                      onClick = {

                        val request = UpadteProfileRequest(
                            email,
                            firstName,
                            lastName,
                            "",
                            "",
                            "",
                            38,
                            gender,
                            address,
                            mobile,
                            processGroupName,
                            OrganizationName,
                            FunctionaryName,
                            "India",
                            stateName,
                            districtname,
                            city,
                            districtCode,
                            stateCode,
                            pincode ,
                            "",
                            usertype,
                            loginId,
                            designation,
                            processGroupCode,
                            OrganizationCode,

                        )

                          updateModel.UpdateProfile(context, request)
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.buttonHeight),

                    shape = RoundedCornerShape(dimens.radiusM),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB)
                    )

                ) {

                    Text(
                        text = "Update Profile",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
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

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 16.dp)
                        ) {
                            Text(
                                text = "View details",
                                color = Color(0xFF123B35),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration =
                                    androidx.compose.ui.text.style.TextDecoration.Underline
                            )

                            Spacer(modifier = Modifier.height(50.dp))

                            Text(
                                text = "$firstName $lastName".trim()
                                    .ifBlank { "Candidate name" },
                                color = Color(0xFF123B35),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box {
                            Box(
                                modifier = Modifier
                                    .size(112.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD5F3EE))
                                    .border(1.dp, Color(0xFF123B35), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    imageUri != null -> Image(
                                        painter = rememberAsyncImagePainter(imageUri),
                                        contentDescription = "Profile",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )

                                    profileBitmap != null -> Image(
                                        bitmap = profileBitmap!!.asImageBitmap(),
                                        contentDescription = "Profile",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )

                                    else -> Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile",
                                        tint = Color(0xFF123B35),
                                        modifier = Modifier.size(62.dp)
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    galleryLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF858585))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit profile image",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    ProfileDetail("Phone", mobile, verified = true)
                    ProfileDetail("Email", email, verified = true)
                    ProfileDetail("Gender", gender,showError = true)
                    ProfileDetail("Date of Birth", age,showError = true)
                    ProfileDetail("Address", address,showError = true)
                    ProfileDetail("loginId", loginId,verified = true)

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= ACCOUNT =================

            SectionTitle("Account Details")
            Spacer(modifier = Modifier.height(20.dp))



            // ================= PERSONAL =================

            SectionTitle("Personal Info")

            InputField(
                firstName,
                { firstName = it },
                "First Name"
            )

            InputField(
                lastName,
                { lastName = it },
                "Last Name"
            )

            InputField(
                value = age,
                onValueChange = {

                    if (
                        it.length <= 3 &&
                        it.all { char -> char.isDigit() }
                    ) {
                        age = it
                    }
                },
                label = "Age",
                keyboardType = KeyboardType.Number
            )

            // ================= BASIC =================



            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {

                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,

                    label = {
                        Text("Gender")
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

                    genderOptions.forEach { option ->

                        DropdownMenuItem(
                            text = {
                                Text(option)
                            },

                            onClick = {

                                gender = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            // ================= CONTACT =================

            SectionTitle("Contact")

            InputField(
                address,
                { address = it },
                "Address"
            )

            // ================= WORK =================

            SectionTitle("Work Info")

            CommonDropdown(
                list = processGroupViewModel.processGroupList,

                selectedText = processGroupName,

                label = "Process Group",

                itemText = {
                    it.level_short_name ?: ""
                },

                onItemSelected = { item ->

                    processGroupName =
                        item.level_short_name ?: ""

                    processGroupCode =
                        item.level_admin_cd ?: ""

                    roleViewModel.fetchRoles(processGroupCode)
                }
            )

            CommonDropdown(
                list = roleViewModel.roleList,

                selectedText = OrganizationName,

                label = "Organization",

                itemText = {
                    it.name_of_the_org ?: ""
                },

                onItemSelected = { item ->

                    OrganizationName =
                        item.name_of_the_org ?: ""

                    OrganizationCode =
                        item.org_id ?: ""

                    functionaryviewModel.fetchFunctionaries(
                        OrganizationCode
                    )
                }
            )

            CommonDropdown(
                list = functionaryviewModel.functionaryList,

                selectedText = FunctionaryName,

                label = "Functionary",

                itemText = {
                    it.user_design ?: ""
                },

                onItemSelected = { item ->

                    FunctionaryName =
                        item.user_design ?: ""

                    stateviewModel.fetchState()
                }
            )

            // ================= LOCATION =================

            SectionTitle("Location")

            InputField(
                country,
                { country = it },
                "Country"
            )

            CommonDropdown(

                list = stateviewModel.stateList,

                selectedText = stateName,

                label = "State",

                itemText = {
                    it.statename
                },

                onItemSelected = { item ->

                    stateName = item.statename

                    stateCode = item.statecode

                    districtviewModel.fetchDistrict(stateCode)
                }
            )

            CommonDropdown(

                list = districtviewModel.districtList,

                selectedText = districtname,

                label = "District",

                itemText = {
                    it.districtname
                },

                onItemSelected = { item ->

                    districtname = item.districtname
                    districtCode = item.districtcode
                }
            )

            InputField(
                city,
                { city = it },
                "City"
            )
            InputField(
                value = pincode,
                onValueChange = {

                    if (
                        it.length <= 6 &&
                        it.all { char -> char.isDigit() }
                    ) {
                        pincode = it
                    }
                },
                label = "Pincode",
                keyboardType = KeyboardType.Number
            )
            // ================= OTHER =================

            SectionTitle("Other")

            InputField(
                designation,
                { designation = it },
                "Designation"
            )

            Spacer(modifier = Modifier.height(120.dp))
        }
    }

    // ================= STATE =================

    when (state) {

        is SignupState.Loading -> {

            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }
        }

        is SignupState.Success -> {

            LaunchedEffect(Unit) {

                navController.navigate("login") {

                    popUpTo("signup") {
                        inclusive = true
                    }
                }
            }
        }

        is SignupState.Error -> {

            error = state.message
        }

        else -> {}
    }
//    LaunchedEffect(authToken) {
//
//    }
    val profileState by profileViewModel.profileState.collectAsState()

    LaunchedEffect(profileState) {

        when (val state = profileState) {

            is Resource.Success -> {

                val response = state.data

                if (response.responseDesc == "OK") {

                    response.wrappedList.forEach { item ->




                        email=item.email
                        firstName=item.firstname
                        lastName=item.lastname
                        age=item.age.toString()
                        gender=item.gender.toString()
                        address=item.address.toString()
                        mobile=item.mobile.toString()
                        designation=item.designation.toString()
                        processGroupName=item.process_group
//                        OrganizationName=item.organization
                        FunctionaryName=item.functionary
                        stateName=item.state
                        districtname=item.district
                        profileBitmap =
                            Base64Utils.base64ToBitmap(
                                item.profileFile
                            )
                    }
                }
            }

            is Resource.Error -> {

                Log.e(
                    "PROFILE",
                    state.message
                )
            }

            is Resource.Loading -> {

                Log.d(
                    "PROFILE",
                    "Loading..."
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun ProfileDetail(
    label: String,
    value: String,
    verified: Boolean = false,
    showError: Boolean = false,
    editable: Boolean = false
) {
    val icon = when (label) {
        "Phone" -> Icons.Default.Phone
        "Email" -> Icons.Default.Email
        "Gender" -> Icons.Default.Person
        "Date of Birth" -> Icons.Default.DateRange
        "Address" -> Icons.Default.LocationOn
        "loginId" -> Icons.Default.Info
        else -> Icons.Default.Info
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Black,
            modifier = Modifier.size(21.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = value.ifBlank { label },
            color = Color(0xFF45424B),
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        if (showError) {
            VerificationError()
        }
        if (verified) {
            VerificationBadge()
        }

        if (editable) {
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF858585)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit $label",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

    }
}
@Composable
private fun VerificationError() {
    Box(
        modifier = Modifier
            .size(19.dp)
            .clip(CircleShape)
            .background(Color(0xFFE53935)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "showError",
            tint = Color.White,
            modifier = Modifier.size(13.dp)
        )
    }
}

@Composable
private fun VerificationBadge() {
    Box(
        modifier = Modifier
            .size(19.dp)
            .clip(CircleShape)
            .background(Color(0xFF18B98B)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Verified",
            tint = Color.White,
            modifier = Modifier.size(13.dp)
        )
    }

}





//@Preview(showBackground = true)
//@Composable
//fun CompleteProfileScreenPreview() {
//
//    CompleteProfileScreen(
//        navController = rememberNavController()
//    )
//}



//@androidx.compose.ui.tooling.preview.Preview(
//    name = "Profile Card",
//    showBackground = true,
//    backgroundColor = 0xFFF3F3F3,
//    widthDp = 390
//)
//@Composable
//private fun ProfileCardPreview() {
//    MaterialTheme {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            shape = RoundedCornerShape(18.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White),
//            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(22.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.Top
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(end = 16.dp)
//                    ) {
//                        Text(
//                            text = "View details",
//                            color = Color(0xFF123B35),
//                            fontSize = 17.sp,
//                            fontWeight = FontWeight.Bold,
//                            textDecoration =
//                                androidx.compose.ui.text.style.TextDecoration.Underline
//                        )
//
//                        Spacer(modifier = Modifier.height(50.dp))
//
//                        Text(
//                            text = "Ajit Ranjan",
//                            color = Color(0xFF123B35),
//                            fontSize = 17.sp,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    }
//
//                    Box {
//                        Box(
//                            modifier = Modifier
//                                .size(112.dp)
//                                .clip(CircleShape)
//                                .background(Color(0xFFD5F3EE))
//                                .border(1.dp, Color(0xFF123B35), CircleShape),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Person,
//                                contentDescription = "Profile",
//                                tint = Color(0xFF123B35),
//                                modifier = Modifier.size(62.dp)
//                            )
//                        }
//
//                        Box(
//                            modifier = Modifier
//                                .align(Alignment.BottomStart)
//                                .size(30.dp)
//                                .clip(CircleShape)
//                                .background(Color(0xFF858585)),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Edit,
//                                contentDescription = "Edit profile image",
//                                tint = Color.White,
//                                modifier = Modifier.size(16.dp)
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(18.dp))
//
//                ProfileDetail("Phone", "9876543210", verified = true)
//                ProfileDetail(
//                    "Email",
//                    "ajit@gmail.com",
//                    verified = true,
//                    editable = true
//                )
//                ProfileDetail("Gender", "Male")
//                ProfileDetail("Date of Birth", "02-03-1992")
//                ProfileDetail("Address", "Delhi, India")
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = "Your Aadhaar eKYC is completed",
//                        color = Color(0xFF123B35),
//                        fontSize = 13.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Spacer(modifier = Modifier.width(6.dp))
//                    VerificationBadge()
//                }
//            }
//        }
//    }
//}





//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedBoxWithConstraintsScope")
//@Composable
//fun CompleteProfileScreen(
//    navController: NavController,
//    viewModel: CompletePofileScreenViewModel = viewModel()
//) {
//     lateinit var appPrefs: AppPreferences
//    val dimens = MaterialTheme.dimens
//    val state = viewModel.state
//    val context = LocalContext.current
//    val scrollState = rememberScrollState()
//
//    var email by remember { mutableStateOf("") }
//    var loginId by remember { mutableStateOf("") }
//
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//
//    var alternateEmail by remember { mutableStateOf("") }
//
//    var dl by remember { mutableStateOf("") }
//
//    var age by remember { mutableStateOf("") }
//    var gender by remember { mutableStateOf("") }
//
//    var address by remember { mutableStateOf("") }
//    var mobile by remember { mutableStateOf("") }
//    var telephone by remember { mutableStateOf("") }
//
//    val coroutineScope = rememberCoroutineScope()
//
//    val errorMap = remember { mutableStateMapOf<String, String>() }
//
//    var processGroupName by remember { mutableStateOf("") }
//    var OrganizationName by remember { mutableStateOf("") }
//    var FunctionaryName by remember { mutableStateOf("") }
//
//    var processGroupCode by remember { mutableStateOf("") }
//    var OrganizationCode by remember { mutableStateOf("") }
//
//    var stateName by remember { mutableStateOf("") }
//    var stateCode by remember { mutableStateOf("") }
//
//    var districtname by remember { mutableStateOf("") }
//
//    var expanded by remember { mutableStateOf(false) }
//
//    val genderOptions = listOf("Male", "Female", "Other")
//
//    var country by remember { mutableStateOf("") }
//    var city by remember { mutableStateOf("") }
//
//    var designation by remember { mutableStateOf("") }
//
//    var error by remember { mutableStateOf("") }
//    var Token by remember { mutableStateOf("") }
//
//    val processGroupViewModel: ProcessGroupViewModel = viewModel()
//    val roleViewModel: RoleViewModel = viewModel()
//    val functionaryviewModel: FunctionaryViewModel = viewModel()
//    val stateviewModel: StateViewModel = viewModel()
//    val districtviewModel: DistrictViewModel = viewModel()
//
//    var profileBitmap by remember {
//        mutableStateOf<Bitmap?>(null)
//    }
//    val viewModelToken: TokenViewModel = viewModel()
//
//    val tokenState by viewModelToken.tokenState.collectAsState()
//    var loginApiCalled by remember { mutableStateOf(false) }
//    var tokenRequestStarted by remember { mutableStateOf(false) }
//    val deviceId = ImeiUtils.getAndroidId(context)
//
//
//
//
//    val versionName = remember {
//        context.packageManager
//            .getPackageInfo(context.packageName, 0)
//            .versionName
//    }
//    appPrefs = AppPreferences(context)
////    val authToken by appPrefs.authToken.collectAsState(initial = null)
//    val userEmail by appPrefs.userEmail.collectAsState(initial = null)
//    val profileViewModel: ProfileViewModel = viewModel()
//
//    val authToken by appPrefs.authToken.collectAsState(initial = null)
//
//    var tokenApiCalled by rememberSaveable {
//        mutableStateOf(false)
//    }
//
//    val userMobile by appPrefs.mobile.collectAsState(initial = null)
//    val processGroup by appPrefs.processGroup.collectAsState(initial = null)
//    val organization by appPrefs.organization.collectAsState(initial = null)
//    val loginID by appPrefs.loginId.collectAsState(initial = null)
//
//    loginId = loginID.toString()
//    email = userEmail.toString()
//    processGroupName = processGroup.toString()
//    OrganizationName = organization.toString()
//    mobile = userMobile.toString()
//
//    // ================= IMAGE =================
//
//    var imageUri by remember {
//        mutableStateOf<Uri?>(null)
//    }
//
//
//
//
//
//    val galleryLauncher =
//        rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.PickVisualMedia()
//        ) { uri ->
//
//            if (uri != null) {
//                imageUri = uri
//            }
//        }
//
//    val cameraLauncher =
//        rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.TakePicturePreview()
//        ) { bitmap: Bitmap? ->
//
//            bitmap?.let {
//
//                val path = MediaStore.Images.Media.insertImage(
//                    context.contentResolver,
//                    it,
//                    "Profile",
//                    null
//                )
//
//                imageUri = Uri.parse(path)
//            }
//        }
//
//    val permissionLauncher =
//        rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.RequestPermission()
//        ) { granted ->
//
//            if (granted) {
//                cameraLauncher.launch(null)
//            }
//        }
//
//    // ================= API =================
//
//    LaunchedEffect(Unit) {
//        processGroupViewModel.fetchProcessGroups()
//    }
//
//    LaunchedEffect(errorMap.size) {
//
//        if (errorMap.isNotEmpty()) {
//
//            coroutineScope.launch {
//                scrollState.animateScrollTo(0)
//            }
//        }
//    }
//
//    // ================= UI =================
//
//    Scaffold(
//
//        containerColor = Color.White,
//
//        // ================= TOPBAR =================
//
//        topBar = {
//
//            TopAppBar(
//
//                title = {
//
//                    Text(
//                        text = "Complete Profile",
//                        fontWeight = FontWeight.SemiBold
//                    )
//                },
//
//                navigationIcon = {
//
//                    IconButton(
//                        onClick = {
//                            navController.popBackStack()
//                        }
//                    ) {
//
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back"
//                        )
//                    }
//                },
//
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color.White
//                )
//            )
//        },
//
//        // ================= BOTTOM BUTTON =================
//
//        bottomBar = {
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//
//                if (error.isNotEmpty()) {
//
//                    Text(
//                        text = error,
//                        color = Color.Red
//                    )
//                }
//
//                Button(
//                    onClick = {
//
//                    },
//
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(dimens.buttonHeight),
//
//                    shape = RoundedCornerShape(dimens.radiusM),
//
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF2563EB)
//                    )
//
//                ) {
//
//                    Text(
//                        text = "Update Profile",
//                        color = Color.White,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//        }
//
//    ) { padding ->
//
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .verticalScroll(scrollState)
//                .padding(horizontal = dimens.screenPaddingHorizontal)
//        ) {
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ================= PROFILE IMAGE =================
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(),
//
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                Box(
//                    contentAlignment = Alignment.BottomEnd
//                ) {
//
//                    Box(
//                        modifier = Modifier
//                            .size(100.dp)
//                            .clip(CircleShape)
//                            .background(
//                                Color(0xFF2563EB).copy(alpha = 0.15f)
//                            ),
//
//                        contentAlignment = Alignment.Center
//                    ) {
//                        when {
//
//                            imageUri != null -> {
//
//                                Image(
//                                    painter = rememberAsyncImagePainter(imageUri),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .clip(CircleShape),
//                                    contentScale = ContentScale.Crop
//                                )
//                            }
//
//                            profileBitmap != null -> {
//
//                                Image(
//                                    bitmap = profileBitmap!!.asImageBitmap(),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .clip(CircleShape),
//                                    contentScale = ContentScale.Crop
//                                )
//                            }
//
//                            else -> {
//
//                                Icon(
//                                    imageVector = Icons.Default.Person,
//                                    contentDescription = null,
//                                    tint = Color(0xFF2563EB),
//                                    modifier = Modifier.size(50.dp)
//                                )
//                            }
//                        }
////                        if (imageUri != null) {
////
////                            Image(
////                                painter = rememberAsyncImagePainter(imageUri),
////                                contentDescription = null,
////
////                                modifier = Modifier
////                                    .fillMaxSize()
////                                    .clip(CircleShape),
////
////                                contentScale = ContentScale.Crop
////                            )
////
////                        } else {
////
////                            Icon(
////                                Icons.Default.Person,
////                                contentDescription = null,
////                                tint = Color(0xFF2563EB),
////                                modifier = Modifier.size(50.dp)
////                            )
////                        }
//                    }
//
//                    // CAMERA BUTTON
//
//                    Box(
//                        modifier = Modifier
//                            .size(32.dp)
//                            .clip(CircleShape)
//                            .background(Color.White)
//                            .clickable {
//
//                                if (
//                                    ContextCompat.checkSelfPermission(
//                                        context,
//                                        Manifest.permission.CAMERA
//                                    ) == PackageManager.PERMISSION_GRANTED
//                                ) {
//
//                                    cameraLauncher.launch(null)
//
//                                } else {
//
//                                    permissionLauncher.launch(
//                                        Manifest.permission.CAMERA
//                                    )
//                                }
//                            },
//
//                        contentAlignment = Alignment.Center
//                    ) {
//
//                        Icon(
//                            Icons.Default.CameraAlt,
//                            contentDescription = null,
//                            tint = Color.Black,
//                            modifier = Modifier.size(18.dp)
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(10.dp))
//
//                Text(
//                    text = "Open Gallery",
//                    color = Color.Blue,
//
//                    modifier = Modifier.clickable {
//
//                        galleryLauncher.launch(
//                            PickVisualMediaRequest(
//                                ActivityResultContracts.PickVisualMedia.ImageOnly
//                            )
//                        )
//                    }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ================= ACCOUNT =================
//
//            SectionTitle("Account Details")
//            Spacer(modifier = Modifier.height(20.dp))
//
//
//
//            InputField(
//                value = loginId,
//                onValueChange = {},
//                label = "Login ID",
//            )
//            InputField(
//                email,
//                { },
//                "Email"
//            )
//
//            // ================= PERSONAL =================
//
//            SectionTitle("Personal Info")
//
//            InputField(
//                firstName,
//                { firstName = it },
//                "First Name"
//            )
//
//            InputField(
//                lastName,
//                { lastName = it },
//                "Last Name"
//            )
//
////            InputField(
////                alternateEmail,
////                { alternateEmail = it },
////                "Alternate Email"
////            )
//
//            // ================= BASIC =================
//
//            SectionTitle("Basic Info")
//
//            InputField(
//                age,
//                { age = it },
//                "Age",
//                KeyboardType.Number
//            )
//
//            ExposedDropdownMenuBox(
//                expanded = expanded,
//                onExpandedChange = {
//                    expanded = !expanded
//                }
//            ) {
//
//                OutlinedTextField(
//                    value = gender,
//                    onValueChange = {},
//                    readOnly = true,
//
//                    label = {
//                        Text("Gender")
//                    },
//
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(
//                            expanded = expanded
//                        )
//                    },
//
//                    modifier = Modifier
//                        .menuAnchor()
//                        .fillMaxWidth()
//                )
//
//                ExposedDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = {
//                        expanded = false
//                    }
//                ) {
//
//                    genderOptions.forEach { option ->
//
//                        DropdownMenuItem(
//                            text = {
//                                Text(option)
//                            },
//
//                            onClick = {
//
//                                gender = option
//                                expanded = false
//                            }
//                        )
//                    }
//                }
//            }
//
//            // ================= CONTACT =================
//
//            SectionTitle("Contact")
//
//            InputField(
//                address,
//                { address = it },
//                "Address"
//            )
//
//            InputField(
//                value = mobile,
//
////                onValueChange = {
////
////                    if (
////                        it.length <= 10 &&
////                        it.all { char -> char.isDigit() }
////                    ) {
////                        mobile = it
////                    }
////                },
//                onValueChange = {},
//
//                label = "Mobile",
//
//                keyboardType = KeyboardType.Number
//            )
//
////            InputField(
////                telephone,
////                { telephone = it },
////                "Telephone"
////            )
//
//            // ================= WORK =================
//
//            SectionTitle("Work Info")
//
//            CommonDropdown(
//                list = processGroupViewModel.processGroupList,
//
//                selectedText = processGroupName,
//
//                label = "Process Group",
//
//                itemText = {
//                    it.level_short_name ?: ""
//                },
//
//                onItemSelected = { item ->
//
//                    processGroupName =
//                        item.level_short_name ?: ""
//
//                    processGroupCode =
//                        item.level_admin_cd ?: ""
//
//                    roleViewModel.fetchRoles(processGroupCode)
//                }
//            )
//
//            CommonDropdown(
//                list = roleViewModel.roleList,
//
//                selectedText = OrganizationName,
//
//                label = "Organization",
//
//                itemText = {
//                    it.name_of_the_org ?: ""
//                },
//
//                onItemSelected = { item ->
//
//                    OrganizationName =
//                        item.name_of_the_org ?: ""
//
//                    OrganizationCode =
//                        item.org_id ?: ""
//
//                    functionaryviewModel.fetchFunctionaries(
//                        OrganizationCode
//                    )
//                }
//            )
//
//            CommonDropdown(
//                list = functionaryviewModel.functionaryList,
//
//                selectedText = FunctionaryName,
//
//                label = "Functionary",
//
//                itemText = {
//                    it.user_design ?: ""
//                },
//
//                onItemSelected = { item ->
//
//                    FunctionaryName =
//                        item.user_design ?: ""
//
//                    stateviewModel.fetchState()
//                }
//            )
//
//            // ================= LOCATION =================
//
//            SectionTitle("Location")
//
//            InputField(
//                country,
//                { country = it },
//                "Country"
//            )
//
//            CommonDropdown(
//
//                list = stateviewModel.stateList,
//
//                selectedText = stateName,
//
//                label = "State",
//
//                itemText = {
//                    it.statename
//                },
//
//                onItemSelected = { item ->
//
//                    stateName = item.statename
//
//                    stateCode = item.statecode
//
//                    districtviewModel.fetchDistrict(stateCode)
//                }
//            )
//
//            CommonDropdown(
//
//                list = districtviewModel.districtList,
//
//                selectedText = districtname,
//
//                label = "District",
//
//                itemText = {
//                    it.districtname
//                },
//
//                onItemSelected = { item ->
//
//                    districtname = item.districtname
//                }
//            )
//
//            InputField(
//                city,
//                { city = it },
//                "City"
//            )
//
//            // ================= OTHER =================
//
//            SectionTitle("Other")
//
//            InputField(
//                designation,
//                { designation = it },
//                "Designation"
//            )
//
//            Spacer(modifier = Modifier.height(120.dp))
//        }
//    }
//
//    // ================= STATE =================
//
//    when (state) {
//
//        is SignupState.Loading -> {
//
//            Box(
//                Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//
//                CircularProgressIndicator()
//            }
//        }
//
//        is SignupState.Success -> {
//
//            LaunchedEffect(Unit) {
//
//                navController.navigate("login") {
//
//                    popUpTo("signup") {
//                        inclusive = true
//                    }
//                }
//            }
//        }
//
//        is SignupState.Error -> {
//
//            error = state.message
//        }
//
//        else -> {}
//    }
////    LaunchedEffect(authToken) {
////
////    }
//
//    authToken?.let { tokenData ->
//
//        profileViewModel.getProfile(
//            token = authToken.toString(),
////                token = "RtqSw0gNXSxGSbtUzN/8Qg==",
//            appVersion =versionName.toString(),
//            loginId = "2532003643",
//            email = userEmail.toString()
//
//        )
//    }
//
//
////    tokenRequestStarted = true
////    loginApiCalled = false
////    viewModelToken.getToken(
////        versionName.toString(),
////        deviceId,
////        "2532003643"
////
////    )
//
//
//
//
//    val profileState by profileViewModel.profileState.collectAsState()
//    LaunchedEffect(profileState) {
//
//        when (val state = profileState) {
//
//            is Resource.Success -> {
//
//                val response = state.data
//
//                if (response.responseDesc == "OK") {
//
//                    response.wrappedList.forEach { item ->
//
//
//                        firstName = item.firstname
//                        lastName = item.lastname
//                        age = item.age.toString()
//                        gender = item.gender.toString()
//                        address = item.address.toString()
//
//                        designation = item.designation.toString()
//
//                        FunctionaryName = item.functionary
//                        stateName = item.state
//                        districtname = item.district
//
//                        profileBitmap =
//                            Base64Utils.base64ToBitmap(item.profileFile)
//                    }
//
//                } else {
//                    Toast.makeText(
//                        context,
//                        response.responseDesc ?: "Something went wrong",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            is Resource.Error -> {
//
//                Toast.makeText(
//                    context,
//                    state.message ?: "Unknown Error",
//                    Toast.LENGTH_LONG
//                ).show()
//
//                Log.e("PROFILE", state.message ?: "Unknown Error")
//            }
//
//            is Resource.Loading -> {
//
////                Toast.makeText(
////                    context,
////                    "Loading Profile...",
////                    Toast.LENGTH_SHORT
////                ).show()
//
//                Log.d("PROFILE", "Loading...")
//            }
//
//            else -> {
//
//                Toast.makeText(
//                    context,
//                    "No Data Found",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                Log.d("PROFILE", "No Data Found")
//            }
//        }
//    }
//
//
//
//}
//
//
//
//@Preview(showBackground = true)
//@Composable
//fun CompleteProfileScreenPreview() {
//
//    CompleteProfileScreen(
//        navController = rememberNavController()
//    )
//}