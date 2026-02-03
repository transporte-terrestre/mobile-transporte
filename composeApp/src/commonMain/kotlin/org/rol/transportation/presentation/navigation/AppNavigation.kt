package org.rol.transportation.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.koinInject
import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.presentation.home_trip.HomeScreen
import org.rol.transportation.presentation.home_trip_detail.TripDetailScreen
import org.rol.transportation.presentation.home_trip_detail_checklist.ChecklistScreen
import org.rol.transportation.presentation.login.LoginScreen
import org.rol.transportation.presentation.profile.ProfileScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val tokenManager: TokenManager = koinInject()

    val startDestination = if (tokenManager.isLoggedIn()) {
        Screen.Home
    } else {
        Screen.Login
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo<Screen.Login> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Screen.Home> {
            HomeScreen(
                onNavigateToTripDetail = { tripId ->
                    navController.navigate(Screen.TripDetail(tripId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile)
                }
            )
        }

        composable<Screen.TripDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TripDetail>()
            TripDetailScreen(
                tripId = args.tripId,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToChecklist = { tripId, tipo ->
                    navController.navigate(Screen.Checklist(tripId, tipo))
                }
            )
        }

        composable<Screen.Checklist> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Checklist>()
            ChecklistScreen(
                tripId = args.tripId,
                tipo = args.tipo,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Login) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

    }
}