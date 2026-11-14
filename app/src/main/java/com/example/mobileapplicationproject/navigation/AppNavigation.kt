
package com.example.mobileapplicationproject.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.*

import com.example.mobileapplicationproject.ui.screens.HomeScreen
import com.example.mobileapplicationproject.ui.theme.screens.JobDetailScreen
import com.example.mobileapplicationproject.ui.theme.screens.OpportunityListScreen
import com.example.mobileapplicationproject.ui.theme.screens.ProfileScreen
import com.example.mobileapplicationproject.viewmodel.HomeViewModel

private val Blue = Color(0xFF2563EB)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

    val currentEntry =
        navController.currentBackStackEntryAsState().value

    val currentRoute = currentEntry?.destination?.route

    val bottomItems = listOf(
        "home" to "Home",
        "saved" to "Saved",
        "applied" to "Applied",
        "profile" to "Profile"
    )

    Scaffold(
        bottomBar = {
            if (currentRoute != "job/{jobId}") {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    bottomItems.forEachIndexed { index, item ->

                        val icon = when (index) {
                            0 -> Icons.Filled.Home
                            1 -> Icons.Outlined.BookmarkBorder
                            2 -> Icons.Outlined.CheckCircle
                            else -> Icons.Filled.Person
                        }

                        NavigationBarItem(
                            selected = currentRoute == item.first,
                            onClick = {
                                navController.navigate(item.first) {
                                    popUpTo("home") {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = item.second
                                )
                            },
                            label = {
                                Text(item.second)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Blue,
                                selectedTextColor = Blue,
                                indicatorColor = Color(0xFFE8F0FF)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            composable("home") {
                HomeScreen(
                    viewModel = homeViewModel,
                    onJobClick = { id ->
                        navController.navigate("job/$id")
                    }
                )
            }

            composable("saved") {
                OpportunityListScreen(
                    title = "Saved Opportunities",
                    jobs = homeViewModel.savedJobs,
                    onJobClick = { id ->
                        navController.navigate("job/$id")
                    },
                    onSaveClick = homeViewModel::toggleSaved
                )
            }

            composable("applied") {
                OpportunityListScreen(
                    title = "My Applications",
                    jobs = homeViewModel.appliedJobs,
                    onJobClick = { id ->
                        navController.navigate("job/$id")
                    },
                    onSaveClick = homeViewModel::toggleSaved
                )
            }

            composable("profile") {
                ProfileScreen()
            }

            composable(
                route = "job/{jobId}",
                arguments = listOf(
                    navArgument("jobId") {
                        type = NavType.IntType
                    }
                )
            ) { entry ->

                val id = entry.arguments?.getInt("jobId")

                val job = homeViewModel.jobs.find {
                    it.id == id
                }

                JobDetailScreen(
                    job = job,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
