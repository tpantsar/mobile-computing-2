package com.codemave.mobicomp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codemave.mobicomp.ui.HomeScreen
import com.codemave.mobicomp.ui.login.LoginScreen
import com.codemave.mobicomp.ui.maps.PaymentLocation
import com.codemave.mobicomp.ui.payment.EditReminder
import com.codemave.mobicomp.ui.payment.MainViewModel
import com.codemave.mobicomp.ui.payment.PaymentScreen

@Composable
fun MainNavigation( /* TODO - Check parameters */
    viewModel: MainViewModel,
    context: Context
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController, context = context)
        }

        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("edit/{id}") {
            val id = it.arguments?.getString("id")
            id?.let {
                EditReminder(navController = navController)
            }
        }

        composable("payments") {
            PaymentScreen(navController = navController)
        }

        composable("map") {
            PaymentLocation(navController = navController)
        }
    }
}