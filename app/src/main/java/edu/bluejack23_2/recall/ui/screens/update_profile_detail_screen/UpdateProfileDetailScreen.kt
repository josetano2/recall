package edu.bluejack23_2.recall.ui.screens.update_profile_detail_screen

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bluejack23_2.recall.R
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.components.CustomSnackbarHost
import edu.bluejack23_2.recall.ui.components.CustomTextField
import edu.bluejack23_2.recall.ui.screens.homepage_screen.HomepageScreen
import edu.bluejack23_2.recall.ui.screens.login_screen.LoginDataUiEvents
import edu.bluejack23_2.recall.ui.screens.profile_screen.ProfileViewModel
import edu.bluejack23_2.recall.ui.theme.BackgroundLightGradient
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes
import kotlinx.coroutines.launch

@Composable
fun UpdateProfileDetailScreen(
    profileViewModel: ProfileViewModel,
    updateProfileDetailViewModel: UpdateProfileDetailViewModel
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState = updateProfileDetailViewModel.uiState.value
    val navController = LocalNav.current
    val userRepository = UserRepository()

    val user by profileViewModel.user.collectAsState(initial = null)
    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(Unit) {
        profileViewModel.getUserData()
    }

    LaunchedEffect(user) {
        updateProfileDetailViewModel.setUser(user!!.username)
    }


    Surface {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = BackgroundLightGradient),
//        color = MaterialTheme.colorScheme.background
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Routes.PROFILE_SCREEN)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_left_arrow),
                            contentDescription = "Profile Detail",
                            modifier = Modifier
                                .size(40.dp)

//                tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Back",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (user != null) {
                        CustomTextField(
                            placeholder = "Username",
                            value = user!!.username,
                            onTextChanged = {
                                updateProfileDetailViewModel.onEvent(
                                    UpdateProfileDetailDataUiEvents.UsernameEntered(it)
                                )
                            })
                    } else {
                        Text("Loading")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            scope.launch {
                                var resp =
                                    updateProfileDetailViewModel.validateUsername(uiState.username)
                                if (resp != "Success") {
                                    snackbarHostState.showSnackbar(resp)
                                } else {
                                    resp = userRepository.updateUsername(user!!, uiState.username)
                                    if (resp != "Success") {
                                        snackbarHostState.showSnackbar(resp)
                                    } else {
                                        snackbarHostState.showSnackbar("Username Updated")
                                        navController.navigate(Routes.PROFILE_SCREEN)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Update Username",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

    }
}