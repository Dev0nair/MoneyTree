package com.ismaelgr.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

class DefaultNavigator @Inject constructor() : Navigator {

    private val _channel: Channel<NavigatorAction> = Channel()
    override val channel: Flow<NavigatorAction> = _channel.consumeAsFlow()

    override fun navigate(destination: String, navOptionsBuilder: NavOptionsBuilder.() -> Unit) {
        _channel.trySend(NavigatorAction.NavigateTo(destination, navOptionsBuilder))
    }

    override fun navigateUp() {
        _channel.trySend(NavigatorAction.NavigateUp)
    }
}