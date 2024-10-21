package com.ismaelgr.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }

@Composable
fun currentNavController() = LocalNavController.current
