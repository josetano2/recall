package edu.bluejack23_2.recall.ui.screens.update_profile_detail_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.screens.login_screen.LoginDataUiEvents
import edu.bluejack23_2.recall.ui.screens.register_screen.RegisterScreenState

class UpdateProfileDetailViewModel : ViewModel() {

    var uiState = mutableStateOf(UpdateProfileDetailScreenState());
    val userRepository = UserRepository()

    fun setUser(username: String) {
        uiState.value = uiState.value.copy(username = username)
    }

    fun onEvent(e: UpdateProfileDetailDataUiEvents) {
        when (e) {
            is UpdateProfileDetailDataUiEvents.UsernameEntered -> {
                uiState.value = uiState.value.copy(
                    username = e.username
                )
            }
        }
    }

    fun validateUsername(username: String): String{
        if(username.length > 15){
            return "Username must be under 15 characters!"
        }

       return "Success"
    }
}

data class UpdateProfileDetailScreenState(
    var username: String = "",
)

sealed class UpdateProfileDetailDataUiEvents {
    data class UsernameEntered(val username: String) : UpdateProfileDetailDataUiEvents();
}