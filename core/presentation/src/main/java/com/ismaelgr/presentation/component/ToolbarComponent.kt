package com.ismaelgr.presentation.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarComponent(
    title: String,
    goBackClick: (() -> Unit)? = null,
    leftSide: @Composable RowScope.() -> Unit = { }
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (goBackClick != null) {
                MyIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, action = goBackClick)
            }
        },
        actions = leftSide
    )
}