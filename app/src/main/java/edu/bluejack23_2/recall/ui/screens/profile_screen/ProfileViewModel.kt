package edu.bluejack23_2.recall.ui.screens.profile_screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.screens.login_screen.LoginScreenState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel(){

    private val userRepository = UserRepository()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        getUserData()
    }

    fun getUserData(){
        viewModelScope.launch {
            try {
                val retrievedUser = userRepository.getUser()
                _user.value = retrievedUser
            }
            catch (e: Exception){}
        }
    }

    suspend fun logout(){
        userRepository.logout()
    }

}