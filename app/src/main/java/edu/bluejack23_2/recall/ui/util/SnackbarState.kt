package edu.bluejack23_2.recall.ui.util

sealed class SnackbarState {
    object None : SnackbarState()
    data class Success(val message: String) : SnackbarState()
    data class Error(val message: String) : SnackbarState()
}