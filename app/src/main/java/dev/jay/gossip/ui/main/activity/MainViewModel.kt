package dev.jay.gossip.ui.main.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jay.gossip.database.User
import dev.jay.gossip.database.UserDAO
import dev.jay.gossip.database.UserDatabase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val db: UserDAO
): ViewModel() {

}