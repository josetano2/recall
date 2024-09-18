package edu.bluejack23_2.recall.ui.screens.login_screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.bluejack23_2.recall.repository.UserRepository
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import edu.bluejack23_2.recall.ui.components.CustomSnackbarHost
import edu.bluejack23_2.recall.ui.components.CustomTextField
import edu.bluejack23_2.recall.ui.components.SnackbarViewModel
import edu.bluejack23_2.recall.ui.screens.register_screen.RegisterDataUiEvents
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, snackbarViewModel: SnackbarViewModel) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState = loginViewModel.uiState.value
    val navController = LocalNav.current
    val userRepository = UserRepository()

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back!",
                    modifier = Modifier
                        .fillMaxWidth(),
//                textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(16.dp))
                CustomTextField(placeholder = "Email", onTextChanged = {
                    loginViewModel.onEvent(
                        LoginDataUiEvents.EmailEntered(it)
                    )
                })
                Spacer(Modifier.height(16.dp))
                CustomTextField(placeholder = "Password", isHidden = true, onTextChanged = {
                    loginViewModel.onEvent(
                        LoginDataUiEvents.PasswordEntered(it)
                    )
                })
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        keyboardController?.hide()
                        scope.launch {
                            var resp: String = loginViewModel.validateUser(
                                uiState.email,
                                uiState.password
                            )
                            if (resp != "Success"
                            ) {
                                snackbarHostState.showSnackbar(resp)
                            } else {
                                resp = userRepository.login(
                                    uiState.email,
                                    uiState.password
                                )
                                if (resp != "Success"
                                ) {
                                    snackbarHostState.showSnackbar(resp)
                                } else {
                                    snackbarHostState.showSnackbar("Success")
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
                        text = "Log in",
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                OutlinedButton(
                    onClick = {
                        navController.navigate(Routes.REGISTER_SCREEN)

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
                        text = "Create a new account",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

}
