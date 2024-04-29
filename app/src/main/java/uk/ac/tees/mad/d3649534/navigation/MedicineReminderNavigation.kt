package uk.ac.tees.mad.d3649534.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3649534.screens.HomeScreen
import uk.ac.tees.mad.d3649534.screens.HomeScreenDestination
import uk.ac.tees.mad.d3649534.screens.SplashScreen
import uk.ac.tees.mad.d3649534.screens.SplashScreenDestination

@Composable
fun MedicineReminderNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SplashScreenDestination.route) {
        composable(SplashScreenDestination.route) {
            SplashScreen(navController = (navController))
        }
        composable(HomeScreenDestination.route) {
            HomeScreen(navController = navController)
        }
    }
}