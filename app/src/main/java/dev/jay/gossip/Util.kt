package dev.jay.gossip

import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

val TextInputLayout.text: String
    get() = editText?.text.toString()

fun <T> oneShotFlow() = MutableSharedFlow<T>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
