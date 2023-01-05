package dev.jay.gossip.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jay.gossip.database.User
import dev.jay.gossip.database.UserDAO
import dev.jay.gossip.database.UserDatabase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val db: UserDAO
) : ViewModel() {
    val verificationId = MutableLiveData<String>()
    var name: String = ""
    var phoneNumber: String =""
    var email: String = ""
    var dateOfBirth = ""

    fun addUser(user: User) {
        viewModelScope.launch {
            db.addUser(user)
        }
    }

}