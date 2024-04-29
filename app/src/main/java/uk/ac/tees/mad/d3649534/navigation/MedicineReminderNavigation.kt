package uk.ac.tees.mad.d3649534.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.tees.mad.d3649534.screens.HomeScreen
import uk.ac.tees.mad.d3649534.screens.HomeScreenDestination
import uk.ac.tees.mad.d3649534.screens.MedicineDetail
import uk.ac.tees.mad.d3649534.screens.MedicineDetailDestination
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
            HomeScreen(navController = navController,)
        }
        composable(
            MedicineDetailDestination.route
//            route = MedicineDetailDestination.routeWithArgs,
//            arguments = listOf(navArgument(MedicineDetailDestination.medicineIdArg) {
//                type = NavType.StringType
//            }
//            )
        ) {
            MedicineDetail()
        }
    }
}