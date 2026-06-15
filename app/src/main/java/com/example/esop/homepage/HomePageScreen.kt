package com.example.esop.homepage

import com.example.esop.ui.theme.CompactDimens
import com.example.esop.ui.theme.Dimens
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esop.network.AppPreferences
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(navController: NavController) {

    // =========================
    // USE YOUR DIMENS HERE
    // =========================

    val dimens = CompactDimens

    val context = LocalContext.current

    val appPrefs = remember {
        AppPreferences(context)
    }
    val userName by appPrefs.userName.collectAsState(initial = null)

    val department by appPrefs.department.collectAsState(initial = "")

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    Toast.makeText(
        context,
        department,
        Toast.LENGTH_SHORT
    ).show()

//    ModalNavigationDrawer(
//
//        drawerState = drawerState,
//
//        drawerContent = {
//
//            ModalDrawerSheet {
//
//                Spacer(
//                    modifier = Modifier.height(dimens.spaceL)
//                )
//
//                Text(
//                    text = "Welcome User",
//                    modifier = Modifier.padding(dimens.spaceM),
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                NavigationDrawerItem(
//                    label = {
//                        Text("Start Test")
//                    },
//                    selected = false,
//                    onClick = {
//
//                        if (
//                            department.equals("Operation", ignoreCase = true) ||
//                            department.equals("Finance", ignoreCase = true)
//                        ) {
//
//
//
//                            navController.navigate("TestInstructionsScreen") {
//
//                                popUpTo("welcome") {
//                                    inclusive = false
//                                }
//                            }
//                        } else {
//
//                            showDialog = true
//                        }
//
//
//
//
//                    }
//                )
//                NavigationDrawerItem(
//
//                    label = {
//                        Text("Logout")
//                    },
//
//                    selected = false,
//
//                    onClick = {
//
//                        scope.launch {
//
//                            // =========================
//                            // CLEAR ALL DATASTORE DATA
//                            // =========================
//
//                            appPrefs.clearUser()
//
//                            // =========================
//                            // NAVIGATE LOGIN
//                            // =========================
//
//                            navController.navigate("login") {
//
//                                popUpTo(0)
//
//                                launchSingleTop = true
//                            }
//                        }
//                    }
//                )
//            }
//        }
//    )
    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = {

                            scope.launch {
                                drawerState.close()
                            }
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Close Drawer"
                        )
                    }

                    Text(
                        text = "Menu",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider()

                Text(
                    text = "Welcome User",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                NavigationDrawerItem(
                    label = {
                        Text("Start Test")
                    },
                    selected = false,
                    onClick = {

                        scope.launch {
                            drawerState.close()
                        }

                        if (
                            department.equals(
                                "Operation",
                                ignoreCase = true
                            ) ||
                            department.equals(
                                "Finance",
                                ignoreCase = true
                            )
                        ) {

                            navController.navigate(
                                "TestInstructionsScreen"
                            ) {

                                popUpTo("welcome") {
                                    inclusive = false
                                }
                            }

                        } else {

                            showDialog = true
                        }
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Text("Logout")
                    },
                    selected = false,
                    onClick = {

                        scope.launch {

                            drawerState.close()

                            appPrefs.clearUser()

                            navController.navigate("login") {

                                popUpTo(0)

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    )


    {

        Scaffold(

            containerColor = Color(0xFFF5F6FA),

            topBar = {

                TopAppBar(

                    modifier = Modifier.height(dimens.toolbarHeight),

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),

                    title = {

                        Column {

                            Text(
//                                text = userName.toString(),
                                text = "eSOP",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Welcome back!",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {

                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                modifier = Modifier.size(dimens.iconM)
                            )
                        }
                    },

                    actions = {

                        IconButton(
                            onClick = {}
                        ) {

                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                modifier = Modifier.size(dimens.iconM)
                            )
                        }
                    }
                )
            },

            bottomBar = {

                NavigationBar(
                    containerColor = Color.White
                ) {

                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = {

                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                modifier = Modifier.size(dimens.iconS)
                            )
                        },
                        label = {
                            Text("Home")
                        }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            Toast.makeText(
                                context,
                                "Not Available",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        icon = {

                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = null,
                                modifier = Modifier.size(dimens.iconS)
                            )
                        },
                        label = {
                            Text("My Tests")
                        }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navController.navigate("ESOPResultScreen")
                        },
                        icon = {

                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = null,
                                modifier = Modifier.size(dimens.iconS)
                            )
                        },
                        label = {
                            Text("Results")
                        }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = {

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(dimens.iconS)
                            )
                        },
                        label = {
                            Text(
                                text = "Profile",
                                modifier = Modifier.clickable {

                                    navController.navigate("CompleteProfileScreen") {

                                        popUpTo("Profile") {
                                            inclusive = false
                                        }
                                    }
                                }
                            )
                        }
//                        label = {
//                            Text("Profile")
//
//                        },


                    )
                }
            }
        )




        { paddingValues ->

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(
                        horizontal = dimens.screenPaddingHorizontal,
                        vertical = dimens.screenPaddingVertical
                    )

            ) {

                // =========================
                // TOP BLUE CARD
                // =========================

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.homeBackgroundBox),

                    shape = RoundedCornerShape(dimens.radiusL),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0A56D6)
                    )
                ) {

                    Row(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimens.spaceL),

                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "eSOP",
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(dimens.spaceXS)
                            )

                            Text(
                                text = "Electronic Standard\nOperation Procedure",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.size(dimens.iconXL)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(dimens.spaceL)
                )

                // =========================
                // QUICK ACTION
                // =========================

                Text(
                    text = "Quick Actions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(dimens.spaceM)
                )

                // =========================
                // START TEST CARD
                // =========================

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.actionCardTotalSyncHeight)
                        .clickable {


                            if (
                                department.equals("Operation", ignoreCase = true) ||
                                department.equals("Finance", ignoreCase = true)
                            ) {


                                navController.navigate("TestInstructionsScreen") {

                                    popUpTo("welcome") {
                                        inclusive = false
                                    }
                                }
                            } else {

                                showDialog = true
                            }
//                            navController.navigate("TestScreen")
//                            navController.navigate("TestInstructionsScreen")
                        },

                    shape = RoundedCornerShape(dimens.radiusL),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2962FF)
                    )
                ) {

                    Row(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimens.spaceL),

                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Start  Test Now",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
//                            Text(
//                                text = "Start New Test",
//                                color = Color.White,
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier.clickable {
//                                    navController.navigate("TestScreen")
//                                }
//                            )

                            Spacer(
                                modifier = Modifier.height(dimens.space2XS)
                            )

                            Text(
                                text = "Attempt a new eSOP exam",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                        }

                        Box(

                            modifier = Modifier
                                .size(dimens.avatarS)
                                .background(
                                    Color.White,
                                    CircleShape
                                ),

                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF2962FF),
                                modifier = Modifier.size(dimens.iconS)
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(dimens.spaceL)
                )

                // =========================
                // GRID CARDS
                // =========================

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    DashboardCard(
                        dimens = dimens,
                        modifier = Modifier.weight(1f)
                            .clickable {
                                Toast.makeText(
                                    context,
                                    "Not Available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },

                        title = "My Tests",
                        subtitle = "View your tests"
                    )

                    Spacer(
                        modifier = Modifier.width(dimens.spaceS)
                    )

                    DashboardCard(
                        dimens = dimens,
                        modifier = Modifier.weight(1f)
                            .clickable {
                                navController.navigate("ESOPResultScreen")
                            },
                        title = "Results",
                        subtitle = "View your results"
                    )
                }

                Spacer(
                    modifier = Modifier.height(dimens.spaceS)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    DashboardCard(
                        dimens = dimens,
                        modifier = Modifier.weight(1f)
                            .clickable {
                                Toast.makeText(
                                    context,
                                    "Not Available Certificate Direct",
                                    Toast.LENGTH_SHORT
                                ).show()
//                                navController.navigate("ESOPCertificateScreen")
                            },
                        title = "Certificate",
                        subtitle = "View & Download"
                    )

                    Spacer(
                        modifier = Modifier.width(dimens.spaceS)
                    )

                    DashboardCard(
                        dimens = dimens,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {

                                navController.navigate("CompleteProfileScreen") {

                                    popUpTo("Profile") {
                                        inclusive = false
                                    }
                                }
                            },

                        title = "Profile",
                        subtitle = "View & Edit"
                    )
                }

                Spacer(
                    modifier = Modifier.height(dimens.spaceXL)
                )
            }
        }

    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text("Department Type Required")
            },
            text = {
                Text(
                    "Please select Department Type and update your profile now."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun DashboardCard(
    dimens: Dimens,
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String
) {

    Card(

        modifier = modifier
            .height(110.dp),

        shape = RoundedCornerShape(dimens.radiusM),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.spaceM),

            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(dimens.spaceXS)
            )

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}














//import android.annotation.SuppressLint
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowForward
//import androidx.compose.material.icons.filled.BarChart
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.List
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Verified
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.DrawerValue
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.ModalDrawerSheet
//import androidx.compose.material3.ModalNavigationDrawer
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.NavigationDrawerItem
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.rememberDrawerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import kotlinx.coroutines.launch

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun WelcomeScreen(navController: NavController) {
//
//    val drawerState = rememberDrawerState(DrawerValue.Closed)
//
//    val scope = rememberCoroutineScope()
//
//    // Scroll State
//    val scrollState = rememberScrollState()
//
//    ModalNavigationDrawer(
//
//        drawerState = drawerState,
//
//        drawerContent = {
//
//            ModalDrawerSheet {
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Text(
//                    text = "Welcome User",
//                    modifier = Modifier.padding(16.dp),
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                NavigationDrawerItem(
//                    label = {
//                        Text("Profile")
//                    },
//                    selected = false,
//                    onClick = {}
//                )
//
//                NavigationDrawerItem(
//                    label = {
//                        Text("Start Test")
//                    },
//                    selected = false,
//                    onClick = {
//
//                        navController.navigate("TestScreen") {
//
//                            popUpTo("welcome") {
//                                inclusive = false
//                            }
//                        }
//                    }
//                )
//
//                NavigationDrawerItem(
//                    label = {
//                        Text("Logout")
//                    },
//                    selected = false,
//                    onClick = {
//
//                        navController.navigate("login") {
//
//                            popUpTo("welcome") {
//                                inclusive = true
//                            }
//                        }
//                    }
//                )
//            }
//        }
//    ) {
//
//        Scaffold(
//
//            containerColor = Color(0xFFF5F6FA),
//
//            topBar = {
//
//                TopAppBar(
//
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = Color.White
//                    ),
//
//                    title = {
//
//                        Column {
//
//                            Text(
//                                text = "Hello, Rahul Kumar",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//
//                            Text(
//                                text = "Welcome back!",
//                                fontSize = 14.sp,
//                                color = Color.Gray
//                            )
//                        }
//                    },
//
//                    navigationIcon = {
//
//                        IconButton(
//                            onClick = {
//
//                                scope.launch {
//                                    drawerState.open()
//                                }
//                            }
//                        ) {
//
//                            Icon(
//                                imageVector = Icons.Default.Menu,
//                                contentDescription = null
//                            )
//                        }
//                    },
//
//                    actions = {
//
//                        IconButton(
//                            onClick = {}
//                        ) {
//
//                            Icon(
//                                imageVector = Icons.Default.Notifications,
//                                contentDescription = null
//                            )
//                        }
//                    }
//                )
//            },
//
//            bottomBar = {
//
//                NavigationBar(
//                    containerColor = Color.White
//                ) {
//
//                    NavigationBarItem(
//                        selected = true,
//                        onClick = {},
//                        icon = {
//
//                            Icon(
//                                imageVector = Icons.Default.Home,
//                                contentDescription = null
//                            )
//                        },
//                        label = {
//                            Text("Home")
//                        }
//                    )
//
//                    NavigationBarItem(
//                        selected = false,
//                        onClick = {},
//                        icon = {
//
//                            Icon(
//                                imageVector = Icons.Default.List,
//                                contentDescription = null
//                            )
//                        },
//                        label = {
//                            Text("My Tests")
//                        }
//                    )
//
//                    NavigationBarItem(
//                        selected = false,
//                        onClick = {},
//                        icon = {
//
//                            Icon(
//                                imageVector = Icons.Default.BarChart,
//                                contentDescription = null
//                            )
//                        },
//                        label = {
//                            Text("Results")
//                        }
//                    )
//
//                    NavigationBarItem(
//                        selected = false,
//                        onClick = {},
//                        icon = {
//
//                            Icon(
//                                imageVector = Icons.Default.Person,
//                                contentDescription = null
//                            )
//                        },
//                        label = {
//                            Text("Profile")
//                        }
//                    )
//                }
//            }
//        ) { paddingValues ->
//
//            // =========================
//            // MAIN SCROLLABLE COLUMN
//            // =========================
//
//            Column(
//
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .verticalScroll(scrollState) // ADD THIS
//                    .padding(16.dp)
//
//            ) {
//
//                // =========================
//                // TOP BLUE CARD
//                // =========================
//
//                Card(
//
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(140.dp),
//
//                    shape = RoundedCornerShape(20.dp),
//
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0xFF0A56D6)
//                    )
//                ) {
//
//                    Row(
//
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(20.dp),
//
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//
//                        Column(
//                            modifier = Modifier.weight(1f)
//                        ) {
//
//                            Text(
//                                text = "eSOP",
//                                color = Color.White,
//                                fontSize = 30.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//
//                            Spacer(
//                                modifier = Modifier.height(8.dp)
//                            )
//
//                            Text(
//                                text = "Electronic Standard\nOperation Process",
//                                color = Color.White,
//                                fontSize = 14.sp
//                            )
//                        }
//
//                        Icon(
//                            imageVector = Icons.Default.Verified,
//                            contentDescription = null,
//                            tint = Color.Green,
//                            modifier = Modifier.size(70.dp)
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // =========================
//                // QUICK ACTION
//                // =========================
//
//                Text(
//                    text = "Quick Actions",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // =========================
//                // START TEST CARD
//                // =========================
//
//                Card(
//
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(100.dp)
//                        .clickable {
//
//                            navController.navigate("TestScreen")
//                        },
//
//                    shape = RoundedCornerShape(20.dp),
//
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0xFF2962FF)
//                    )
//                ) {
//
//                    Row(
//
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(20.dp),
//
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//
//                        Column(
//                            modifier = Modifier.weight(1f)
//                        ) {
//
//                            Text(
//                                text = "Start New Test",
//                                color = Color.White,
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//
//                            Spacer(
//                                modifier = Modifier.height(4.dp)
//                            )
//
//                            Text(
//                                text = "Attempt a new eSOP exam",
//                                color = Color.White.copy(alpha = 0.8f),
//                                fontSize = 14.sp
//                            )
//                        }
//
//                        Box(
//
//                            modifier = Modifier
//                                .size(45.dp)
//                                .background(
//                                    Color.White,
//                                    CircleShape
//                                ),
//
//                            contentAlignment = Alignment.Center
//                        ) {
//
//                            Icon(
//                                imageVector = Icons.Default.ArrowForward,
//                                contentDescription = null,
//                                tint = Color(0xFF2962FF)
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // =========================
//                // GRID CARDS
//                // =========================
//
//                Row(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//
//                    DashboardCard(
//                        modifier = Modifier.weight(1f),
//                        title = "My Tests",
//                        subtitle = "View your tests"
//                    )
//
//                    Spacer(
//                        modifier = Modifier.width(12.dp)
//                    )
//
//                    DashboardCard(
//                        modifier = Modifier.weight(1f),
//                        title = "Results",
//                        subtitle = "View your results"
//                    )
//                }
//
//                Spacer(
//                    modifier = Modifier.height(12.dp)
//                )
//
//                Row(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//
//                    DashboardCard(
//                        modifier = Modifier.weight(1f),
//                        title = "Certificate",
//                        subtitle = "View & Download"
//                    )
//
//                    Spacer(
//                        modifier = Modifier.width(12.dp)
//                    )
//
//                    DashboardCard(
//                        modifier = Modifier.weight(1f),
//                        title = "Profile",
//                        subtitle = "View & Edit"
//                    )
//                }
//
//                // Extra Bottom Space
//                Spacer(modifier = Modifier.height(30.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun DashboardCard(
//    modifier: Modifier = Modifier,
//    title: String,
//    subtitle: String
//) {
//
//    Card(
//
//        modifier = modifier
//            .height(110.dp),
//
//        shape = RoundedCornerShape(18.dp),
//
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        )
//    ) {
//
//        Column(
//
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//
//            verticalArrangement = Arrangement.Center
//        ) {
//
//            Text(
//                text = title,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(
//                modifier = Modifier.height(6.dp)
//            )
//
//            Text(
//                text = subtitle,
//                fontSize = 13.sp,
//                color = Color.Gray
//            )
//        }
//    }
//}











































//import android.annotation.SuppressLint
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material3.DrawerValue
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.ModalDrawerSheet
//import androidx.compose.material3.ModalNavigationDrawer
//import androidx.compose.material3.NavigationDrawerItem
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.rememberDrawerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import kotlinx.coroutines.launch

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun WelcomeScreen(navController: NavController) {
//
//    val drawerState = rememberDrawerState(DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//
//    ModalNavigationDrawer(
//        drawerContent = {
//            ModalDrawerSheet {
//
//                Text("Welcome User", modifier = Modifier.padding(16.dp))
//
//                NavigationDrawerItem(
//                    label = { Text("Profile") },
//                    selected = false,
//                    onClick = {}
//                )
//
//                NavigationDrawerItem(
//                    label = { Text("Logout") },
//                    selected = false,
//                    onClick = {
//                        navController.navigate("login") {
//                            popUpTo("welcome") { inclusive = true }
//                        }
//                    }
//                )
//
//                NavigationDrawerItem(
//                    label = { Text("Start Test") },
//                    selected = false,
//                    onClick = {
//                        navController.navigate("TestScreen") {
//                            popUpTo("welcome") { inclusive = true }
//                        }
//                    }
//                )
//            }
//        },
//        drawerState = drawerState
//    ) {
//
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Dashboard") },
//                    navigationIcon = {
//                        IconButton(onClick = {
//                            scope.launch { drawerState.open() }
//                        }) {
//                            Icon(Icons.Default.Menu, contentDescription = "")
//                        }
//                    }
//                )
//            }
//        )
//        {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("Welcome Page 🎉")
//            }
//        }
//    }
//}