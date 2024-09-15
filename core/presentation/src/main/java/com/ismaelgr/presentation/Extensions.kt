package com.ismaelgr.presentation

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.getWeekName(): String =
    SimpleDateFormat(
        "EEEE",
        Locale.getDefault()
    )
        .format(this)
        .replaceFirstChar { it.uppercase() }

fun Date.getInfo(style: Int = DateFormat.LONG): String =
    DateFormat
        .getDateInstance(style)
        .format(this)

fun String.onlyFirstUpper() =
    this
        .lowercase(Locale.getDefault())
        .replaceFirstChar { it.uppercase() }

@Composable
fun Uri.toDrawable(): Drawable? {
    return if (File(this.path.orEmpty()).isFile) {
        val inputStream = LocalContext.current.contentResolver.openInputStream(this)
        Drawable.createFromStream(
            inputStream,
            this.toString()
        )
    } else {
        null
    }
}

fun <R> ViewModel.runUseCase(
    flow: Flow<R>,
    onEach: (R) -> Unit = {}, onComplete: () -> Unit = {}, onError: (Throwable) -> Unit = {}
) {
    flow.flowUseCase(onEach, onComplete, onError, viewModelScope)
}

fun <R> Flow<R>.flowUseCase(onEach: (R) -> Unit = {}, onComplete: () -> Unit = {}, onError: (Throwable) -> Unit = {}, coroutineScope: CoroutineScope) {
    this.flowOn(Dispatchers.IO)
        .onEach { item ->
            withContext(Dispatchers.Main) {
                onEach(item)
            }
        }
        .onCompletion {
            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
        .launchIn(coroutineScope)
}

fun Date.toUIDate(): String {
    return SimpleDateFormat("EEEE dd/MM ", Locale.getDefault()).format(this).onlyFirstUpper()
}