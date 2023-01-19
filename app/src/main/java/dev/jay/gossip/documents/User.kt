package dev.jay.gossip.documents

import android.media.Image

data class User (
    val name: String,
    val email: String,
    val phone: String,
    val bio: String,
    val country: String,
    val dateOfBirth: String,
    val profile: String,
    val uid: String
    )