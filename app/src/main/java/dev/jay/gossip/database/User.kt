package dev.jay.gossip.database

import androidx.room.Entity
import org.jetbrains.annotations.Nullable

@Entity
data class User(
    val name: String,
    @Nullable val photo: String,
    @Nullable val phone: String,
    @Nullable val email: String,
    @Nullable val bio: String,
    @Nullable val dateOfBirth: Long,
    @Nullable val country: String,
    @Nullable val password: String
)