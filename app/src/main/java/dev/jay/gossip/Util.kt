package dev.jay.gossip

import com.google.android.material.textfield.TextInputLayout

val TextInputLayout.text: String
    get() = editText?.text.toString()