package com.ismaelgr.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MyIcon(
    imageVector: ImageVector,
    action: (() -> Unit)? = null,
    withBox: Boolean = true
) {
    if (withBox) {
        Icon(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                .padding(7.dp)
                .size(24.dp)
                .let {
                    if (action != null) it.clickable { action() } else it
                },
            tint = Color.Black,
            imageVector = imageVector, contentDescription = imageVector.name
        )
    } else {
        Icon(
            modifier = Modifier
                .padding(7.dp)
                .size(24.dp)
                .let {
                    if (action != null) it.clickable { action() } else it
                },
            tint = MaterialTheme.colorScheme.primary,
            imageVector = imageVector, contentDescription = imageVector.name
        )
    }
    
}