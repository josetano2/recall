package edu.bluejack23_2.recall.ui.util

import ScoredCard
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.bluejack23_2.recall.ui.components.SnackbarViewModel
import edu.bluejack23_2.recall.ui.screens.*
import edu.bluejack23_2.recall.ui.screens.add_deck_screen.AddDeckScreen
import edu.bluejack23_2.recall.ui.screens.deck_detail_screen.DeckDetailScreen
import edu.bluejack23_2.recall.ui.screens.explore_screen.ExploreScreen
import edu.bluejack23_2.recall.ui.screens.homepage_screen.HomepageScreen
import edu.bluejack23_2.recall.ui.screens.login_screen.LoginScreen
import edu.bluejack23_2.recall.ui.screens.login_screen.LoginViewModel
import edu.bluejack23_2.recall.ui.screens.profile_screen.ProfileScreen
import edu.bluejack23_2.recall.ui.screens.profile_screen.ProfileViewModel
import edu.bluejack23_2.recall.ui.screens.ranked_screen.RankedScreen
import edu.bluejack23_2.recall.ui.screens.recall_screen.RecallFinishPage
import edu.bluejack23_2.recall.ui.screens.recall_screen.RecallScreen
import edu.bluejack23_2.recall.ui.screens.register_screen.RegisterScreen
import edu.bluejack23_2.recall.ui.screens.register_screen.RegisterViewModel
import edu.bluejack23_2.recall.ui.screens.update_profile_detail_screen.UpdateProfileDetailScreen
import edu.bluejack23_2.recall.ui.screens.update_profile_detail_screen.UpdateProfileDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationGraph(
    registerViewModel: RegisterViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    updateProfileDetailViewModel: UpdateProfileDetailViewModel = viewModel(),
    snackbarViewModel: SnackbarViewModel = viewModel(),
    promptManager: BiometricPromptManager
) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNav provides navController) {
        NavHost(navController = navController, startDestination = Routes.LANDING_SCREEN) {
            composable(Routes.LANDING_SCREEN) {
                LandingScreen()
            }
            composable(Routes.REGISTER_SCREEN) {
                RegisterScreen(registerViewModel, snackbarViewModel)
            }
            composable(Routes.LOGIN_SCREEN) {
                LoginScreen(loginViewModel, snackbarViewModel)
            }
            composable(Routes.HOME_SCREEN) {
                HomepageScreen(profileViewModel = profileViewModel, promptManager = promptManager)
            }
            composable(Routes.PROFILE_SCREEN) {
                HomepageScreen(
                    selectedIndex = Routes.PROFILE_SCREEN_INDEX,
                    profileViewModel = profileViewModel,
                    promptManager = promptManager
                )
            }
            composable(Routes.PROFILE_UPDATE_DETAIL_SCREEN) {
                UpdateProfileDetailScreen(profileViewModel, updateProfileDetailViewModel)
            }
            composable(
                Routes.DECK_DETAIL_SCREEN + "/{deckId}",
                arguments = listOf(navArgument("deckId") { type = NavType.StringType })
            ) { backStackEntry ->
                val deckId = backStackEntry.arguments?.getString("deckId")
                DeckDetailScreen(deckId = deckId ?: "")
            }

            composable(
                Routes.RECALL_SCREEN + "/{deckId}/{mode}/{numberOfCards}",
                arguments = listOf(
                    navArgument("deckId") { type = NavType.StringType },
                    navArgument("mode") { type = NavType.StringType },
                    navArgument("numberOfCards") {type=NavType.IntType})
            ){backStackentry ->
                val deckId = backStackentry.arguments?.getString("deckId")
                val mode = backStackentry.arguments?.getString("mode")
                val numberOfCards = backStackentry.arguments?.getInt("numberOfCards")
                if (deckId != null && mode != null && numberOfCards != null){
                    RecallScreen(deckId = deckId, mode = mode, numberOfCards = numberOfCards);
                }
            }

            composable(Routes.RECALL_FINISH_SCREEN + "/{mode}",
                arguments = listOf(
                    navArgument("mode") {type = NavType.StringType}
                )
                ) { backStackEntry ->
                val storedCards = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<List<ScoredCard>>("storedCards")
                val mode = backStackEntry.arguments?.getString("mode")
                if (mode != null){
                    RecallFinishPage(storedCards = storedCards, mode = mode)
                }
            }
        }

//        composable(Routes.EXPLORE_SCREEN) {
//            ExploreScreen(navController)
//        }
//        composable(Routes.ADD_DECK_SCREEN) {
//            AddDeckScreen(navController)
//        }
//        composable(Routes.RANKED_SCREEN) {
//            RankedScreen(navController)
//        }
//        composable(Routes.PROFILE_SCREEN) {
//            ProfileScreen(navController)
//        }

    }

}

