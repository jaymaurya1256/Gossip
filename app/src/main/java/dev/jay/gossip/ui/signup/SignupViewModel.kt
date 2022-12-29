package dev.jay.gossip.ui.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jay.gossip.database.User
import dev.jay.gossip.database.UserDAO
import dev.jay.gossip.database.UserDatabase
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val db: UserDAO
) : ViewModel() {

    lateinit var user: User

    suspend fun addUser() = db.addUser(user)
}