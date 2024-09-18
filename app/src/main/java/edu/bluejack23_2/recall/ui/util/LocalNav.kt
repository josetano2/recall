package edu.bluejack23_2.recall.ui.util

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNav = staticCompositionLocalOf<NavHostController> { error("Not provided") }