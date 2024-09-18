package edu.bluejack23_2.recall.ui.screens.login_screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.screens.register_screen.RegisterDataUiEvents
import edu.bluejack23_2.recall.ui.screens.register_screen.RegisterScreenState
import edu.bluejack23_2.recall.ui.util.LocalNav
import edu.bluejack23_2.recall.ui.util.Routes
import edu.bluejack23_2.recall.ui.util.SnackbarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    var uiState = mutableStateOf(LoginScreenState())
    val userRepository = UserRepository()



    fun onEvent(e: LoginDataUiEvents) {
        when (e) {
            is LoginDataUiEvents.EmailEntered -> {
                uiState.value = uiState.value.copy(
                    email = e.email
                )
            }
            is LoginDataUiEvents.PasswordEntered -> {
                uiState.value = uiState.value.copy(
                    password = e.password
                )
            }
        }
    }

    fun validateUser(email: String, password: String) : String{
        // Validate empty
        if(email.isNullOrEmpty()){

            return "Email must be filled!";
        }

        if(password.isNullOrEmpty()){

            return "Password must be filled!"
        }
        return "Success"
    }

}



data class LoginScreenState(
    var email: String = "",
    var password: String = "",
)

sealed class LoginDataUiEvents {
    data class EmailEntered(val email: String) : LoginDataUiEvents();
    data class PasswordEntered(val password: String) : LoginDataUiEvents();
}

interface LoginResultCallback {
    fun onSuccess()
    fun onError(errorMessage: String)
}