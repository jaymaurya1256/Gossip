package dev.jay.gossip.ui.main.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class MainViewModel: ViewModel() {
    lateinit var userUID: MutableLiveData<String>

    fun initUserUID(user: String) {
        userUID.value = user
    }

}