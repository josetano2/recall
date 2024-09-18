package edu.bluejack23_2.recall.ui.screens.register_screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.components.CustomSnackbarHost
import edu.bluejack23_2.recall.ui.components.CustomSpacer
import edu.bluejack23_2.recall.ui.components.CustomTextField
import edu.bluejack23_2.recall.ui.components.SnackbarViewModel
import edu.bluejack23_2.recall.ui.theme.BackgroundLightGradient
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes
import kotlinx.coroutines.launch
import androidx.compose.material.Scaffold
@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, snackbarViewModel: SnackbarViewModel) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = registerViewModel.uiState.value
    val navController = LocalNav.current
    val userRepository = UserRepository()

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
    ) {


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = BackgroundLightGradient),
//        color = MaterialTheme.colorScheme.background
//        brush = BackgroundLightGradient
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Get started with recall!",
                    modifier = Modifier
                        .fillMaxWidth(),
//                textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(16.dp))
                CustomTextField(placeholder = "Username", onTextChanged = {
                    registerViewModel.onEvent(
                        RegisterDataUiEvents.UsernameEntered(it)
                    )
                })
                Spacer(Modifier.height(16.dp))
                CustomTextField(placeholder = "Email", onTextChanged = {
                    registerViewModel.onEvent(
                        RegisterDataUiEvents.EmailEntered(it)
                    )
                })
                Spacer(Modifier.height(16.dp))
                CustomTextField(placeholder = "Password", isHidden = true, onTextChanged = {
                    registerViewModel.onEvent(
                        RegisterDataUiEvents.PasswordEntered(it)
                    )
                })
                Spacer(Modifier.height(16.dp))
                CustomTextField(placeholder = "Confirm Password", isHidden = true, onTextChanged = {
                    registerViewModel.onEvent(
                        RegisterDataUiEvents.ConfirmPasswordEntered(it)
                    )
                })
                CustomSpacer()
                Button(
                    onClick = {
                        keyboardController?.hide()
                        scope.launch {
                            Log.d("asdasda", "adsasa")
                            var resp: String = registerViewModel.validateUser(
                                uiState.username,
                                uiState.email,
                                uiState.password,
                                uiState.confirmPassword
                            )

                            if (resp != "Success"
                            ) {
                                Log.d("asdsaad", resp)
                                snackbarHostState.showSnackbar(resp)
                            } else {
                                resp = userRepository.createUser(
                                    uiState.username,
                                    uiState.email,
                                    uiState.password
                                )
                                if (resp != "Success"
                                ) {
                                    snackbarHostState.showSnackbar(resp)
                                } else {
                                    snackbarHostState.showSnackbar(resp)
                                    navController.navigate(Routes.HOME_SCREEN)
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
                        text = "Register account",
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                OutlinedButton(
                    onClick = {
                        navController.navigate(Routes.LOGIN_SCREEN)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Have an account? Click here!",
                        fontSize = 16.sp
                    )
                }
            }

        }
    }
}
