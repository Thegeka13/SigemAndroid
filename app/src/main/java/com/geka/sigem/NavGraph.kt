package com.geka.sigem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.geka.sigem.screens.HomeScreen
import com.geka.sigem.screens.LoginScreen
import com.geka.sigem.screens.MarketScreen

@Composable
fun AppNavHost(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val isLoggedInState by authViewModel.isLoggedIn.collectAsState()

    // until we know session, don't render nav host (could show splash)
    if (isLoggedInState == null) {
        // loading state - simple blank or loading indicator
        return
    }

    // Recreate NavHost when start destination changes
    key(isLoggedInState) {
        val start = if (isLoggedInState == true) Screen.Home.route else Screen.Login.route

        NavHost(navController = navController, startDestination = start) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLogin = { password ->
                        authViewModel.login(password) { success ->
                            if (success) {
                                // navigate to home and clear backstack
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            } else {
                                // keep on screen; LoginScreen shows error
                            }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToMarket = {
                        navController.navigate(Screen.Market.route)
                    },
                    onLogout = {
                        authViewModel.logout {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(Screen.Market.route) {
                MarketScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
