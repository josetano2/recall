package edu.bluejack23_2.recall.ui.components

import androidx.lifecycle.ViewModel
import edu.bluejack23_2.recall.ui.util.SnackbarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SnackbarViewModel : ViewModel() {

    private val _snackbarState= MutableStateFlow<SnackbarState>(SnackbarState.None)
    val snackbarState = _snackbarState.asStateFlow()

    fun showSuccess(resp: String) {
        _snackbarState.value = SnackbarState.Success(resp)
    }

    fun showError(resp: String) {
        _snackbarState.value = SnackbarState.Error(resp)
    }

}