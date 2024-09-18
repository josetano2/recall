package edu.bluejack23_2.recall.ui.screens.homepage_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.bluejack23_2.recall.R
import edu.bluejack23_2.recall.ui.theme.*

import edu.bluejack23_2.recall.ui.components.CustomSnackbarHost
import edu.bluejack23_2.recall.ui.screens.add_deck_screen.AddDeckScreen
import edu.bluejack23_2.recall.ui.screens.explore_screen.ExploreScreen
import edu.bluejack23_2.recall.ui.screens.profile_screen.ProfileScreen
import edu.bluejack23_2.recall.ui.screens.profile_screen.ProfileViewModel
import edu.bluejack23_2.recall.ui.screens.ranked_screen.RankedScreen
import edu.bluejack23_2.recall.ui.util.BiometricPromptManager
import edu.bluejack23_2.recall.ui.util.Routes
import androidx.compose.material.Scaffold
import androidx.compose.material3.SnackbarHostState


@ExperimentalFoundationApi
@Composable
fun HomepageScreen(
    selectedIndex: Int = 0,
    profileViewModel: ProfileViewModel,
    promptManager: BiometricPromptManager
) {
    var selectedScreenIndex by remember { mutableStateOf(selectedIndex) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {

            when (selectedScreenIndex) {
                0 -> HomepageActualScreen()
                1 -> ExploreScreen()
                2 -> AddDeckScreen(snackbarHostState = snackbarHostState)
                3 -> RankedScreen()
                4 -> ProfileScreen(profileViewModel, promptManager)
                else -> HomepageActualScreen()
            }

            BottomMenu(
                items = listOf(
                    BottomMenuContent("Home", R.drawable.ic_home, Routes.HOME_SCREEN),
                    BottomMenuContent("Explore", R.drawable.ic_search, Routes.EXPLORE_SCREEN),
                    BottomMenuContent("Create Deck", R.drawable.ic_add_2, Routes.ADD_DECK_SCREEN),
                    BottomMenuContent("Ranked", R.drawable.ic_trophy, Routes.RANKED_SCREEN),
                    BottomMenuContent("Profile", R.drawable.ic_profile, Routes.PROFILE_SCREEN),
                ),
                modifier = Modifier.align(Alignment.BottomCenter),
                initialSelectedItemIndex = selectedScreenIndex,
                onItemSelected = { index ->
                    selectedScreenIndex = index
                }
            )
        }
    }
}


@Composable
fun BottomMenu(
    items: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    activeHighlightColor: Color = PrimaryDark,
    activeTextColor: Color = Color.White,
    inactiveTextColor: Color = TertiaryDark,
    initialSelectedItemIndex: Int = 0,
    onItemSelected: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(SecondaryDark)
            .padding(16.dp)
    ) {
        items.forEachIndexed { index, item ->
            BottomMenuItem(
                item = item,
                isSelected = index == initialSelectedItemIndex,
                activeHighlightColor = activeHighlightColor,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveTextColor
            ) {
                onItemSelected(index) // Update selected item index
//                navController.navigate(item.route)
            }
        }
    }
}

@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected: Boolean = false,
    activeHighlightColor: Color = ButtonBlue,
    activeTextColor: Color = TextDark,
    inactiveTextColor: Color = AquaBlue,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable(onClick = onItemClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) activeHighlightColor else Color.Transparent)
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconId),
                contentDescription = item.title,
                tint = if (isSelected) activeTextColor else inactiveTextColor,
                modifier = Modifier.size(26.dp)
            )
        }
//        Text(
//            text = item.title,
//            color = if (isSelected) activeTextColor else inactiveTextColor
//        )
    }
}


