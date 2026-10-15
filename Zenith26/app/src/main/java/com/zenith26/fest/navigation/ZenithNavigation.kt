package com.zenith26.fest.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zenith26.fest.ui.admin.AdminDashboardScreen
import com.zenith26.fest.ui.student.StudentPortalScreen
import com.zenith26.fest.ui.viewmodel.AdminViewModel
import com.zenith26.fest.ui.viewmodel.StudentViewModel

sealed class Screen(val route: String) {
    object StudentPortal : Screen("student_portal")
    object AdminDashboard : Screen("admin_dashboard")
}

@Composable
fun ZenithNavHost(
    navController: NavHostController = rememberNavController(),
    studentViewModel: StudentViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.StudentPortal.route
    ) {
        composable(Screen.StudentPortal.route) {
            StudentPortalScreen(
                viewModel = studentViewModel,
                onNavigateToAdmin = {
                    adminViewModel.loadAllData()
                    navController.navigate(Screen.AdminDashboard.route)
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                viewModel = adminViewModel,
                onNavigateToStudent = {
                    studentViewModel.loadEvents()
                    navController.navigate(Screen.StudentPortal.route) {
                        popUpTo(Screen.StudentPortal.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
