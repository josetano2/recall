package edu.bluejack23_2.recall.ui.screens.register_screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.repository.UserRepository

class RegisterViewModel : ViewModel() {

    var uiState = mutableStateOf(RegisterScreenState());
    val userRepository = UserRepository()

    fun onEvent(e: RegisterDataUiEvents) {
        when (e) {
            is RegisterDataUiEvents.UsernameEntered -> {
                uiState.value = uiState.value.copy(
                    username = e.username
                )
            }
            is RegisterDataUiEvents.EmailEntered -> {
                uiState.value = uiState.value.copy(
                    email = e.email
                )
            }
            is RegisterDataUiEvents.PasswordEntered -> {
                uiState.value = uiState.value.copy(
                    password = e.password
                )
            }
            is RegisterDataUiEvents.ConfirmPasswordEntered -> {
                uiState.value = uiState.value.copy(
                    confirmPassword = e.confirmPassword
                )
            }

        }
    }

    fun validateUser(username: String, email: String, password: String, confirmPassword: String) : String{

        if(username.isNullOrEmpty()){

            return "Username must be filled!";
        }

        if(email.isNullOrEmpty()){

            return "Email must be filled!";
        }

        if(password.isNullOrEmpty()){

            return "Password must be filled!"
        }

        if(confirmPassword.isNullOrEmpty()){

            return "Confirm password must be filled!"
        }

        if(username.length > 15){
            return "Username must be under 15 characters!"
        }

        if(!email.contains("@")){
            return "Email must be valid!"
        }

        if(password.length < 8){
            return "Password must be 8 characters long"
        }

        if(password != confirmPassword){
            return "Confirm password must be the same with password!"
        }

        return "Success"

    }
}

data class RegisterScreenState(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = ""
)

sealed class RegisterDataUiEvents {
    data class UsernameEntered(val username: String) : RegisterDataUiEvents();
    data class EmailEntered(val email: String) : RegisterDataUiEvents();
    data class PasswordEntered(val password: String) : RegisterDataUiEvents();
    data class ConfirmPasswordEntered(val confirmPassword: String) : RegisterDataUiEvents();
}