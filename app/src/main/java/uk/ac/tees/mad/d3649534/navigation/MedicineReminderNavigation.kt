package uk.ac.tees.mad.d3649534.navigation

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.auth.AskLoginDestination
import uk.ac.tees.mad.d3649534.auth.AskLoginScreen
import uk.ac.tees.mad.d3649534.auth.GoogleAuthUiClient
import uk.ac.tees.mad.d3649534.screens.EditMedication
import uk.ac.tees.mad.d3649534.screens.EditMedicationDestination
import uk.ac.tees.mad.d3649534.screens.HomeScreen
import uk.ac.tees.mad.d3649534.screens.HomeScreenDestination
import uk.ac.tees.mad.d3649534.screens.MedicationHistory
import uk.ac.tees.mad.d3649534.screens.MedicationHistoryDestination
import uk.ac.tees.mad.d3649534.screens.MedicineDetail
import uk.ac.tees.mad.d3649534.screens.MedicineDetailDestination
import uk.ac.tees.mad.d3649534.screens.ProfileDestination
import uk.ac.tees.mad.d3649534.screens.ProfileScreen
import uk.ac.tees.mad.d3649534.screens.SplashScreen
import uk.ac.tees.mad.d3649534.screens.SplashScreenDestination
import uk.ac.tees.mad.d3649534.viewmodels.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedicineReminderNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    NavHost(navController = navController, startDestination = SplashScreenDestination.route) {
        composable(SplashScreenDestination.route) {
            SplashScreen(navController = (navController))
        }
        composable(HomeScreenDestination.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = MedicineDetailDestination.routeWithArgs,
            arguments = listOf(navArgument(MedicineDetailDestination.medicineIdArg) {
                type = NavType.StringType
            })
        ) {
            MedicineDetail(
                onNavigateUp = {

                    navController.navigateUp()
                },
                onChangeSchedule = {
                    navController.navigate(EditMedicationDestination.route + "/" + it)
                }
            )
        }
        composable(
            route = EditMedicationDestination.routeWithArgs,
            arguments = listOf(navArgument(EditMedicationDestination.medicineIdArg) {
                type = NavType.StringType
            })
        ) {
            EditMedication(
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(ProfileDestination.route) {
            ProfileScreen(
                currentUser = googleAuthUiClient.getSignedInUser(),
                onSignOut = {
                    scope.launch {
                        googleAuthUiClient.signOut()
                    }
                    navController.navigate(HomeScreenDestination.route)
                    Toast.makeText(
                        context,
                        "User Signed out",
                        Toast.LENGTH_LONG
                    ).show()
                },
                onSignIn = { navController.navigate(AskLoginDestination.route) },
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigationHistory = {
                    navController.navigate(MedicationHistoryDestination.route)
                }
            )
        }

        composable(MedicationHistoryDestination.route) {
            MedicationHistory(
                onMedicationItemClick = {
                    navController.navigate(MedicineDetailDestination.route + "/" + it)
                },
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = AskLoginDestination.route) {
            val viewModel: LoginViewModel = viewModel()
            val state = viewModel.state.collectAsState().value

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(HomeScreenDestination.route)
                    viewModel.resetState()
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        scope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInWithGoogleResult(signInResult)
                        }
                    }
                }
            )


            AskLoginScreen(
                loginWithGoogle = {
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )

                    }
                },
                onSkip = { navController.popBackStack() }
            )
        }

    }
}