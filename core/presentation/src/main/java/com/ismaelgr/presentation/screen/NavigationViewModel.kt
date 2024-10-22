package com.ismaelgr.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptionsBuilder
import com.ismaelgr.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class NavigationViewModel @Inject constructor(private val navigator: Navigator) : ViewModel() {
    fun navigate(destination: String, navOptionsBuilder: NavOptionsBuilder.() -> Unit = {}) {
        navigator.navigate(destination, navOptionsBuilder)
    }

    fun navigateUp() {
        navigator.navigateUp()
    }
}