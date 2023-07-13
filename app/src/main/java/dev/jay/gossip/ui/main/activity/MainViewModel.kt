package dev.jay.gossip.ui.main.activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jay.gossip.oneShotFlow
import javax.inject.Inject

private const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel @Inject constructor(
): ViewModel() {

    val userEmail = MutableLiveData("")
    val userProfileImage = MutableLiveData("")
    val userName = MutableLiveData("")

    val goToSignup = oneShotFlow<Boolean>()
    fun checkUserRegistered() {
        if (Firebase.auth.currentUser == null) {
            goToSignup.tryEmit(true)
            return
        }
        // Check if the user is registered or not
        Firebase.firestore.collection("users").document(Firebase.auth.currentUser!!.uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.getString("name").isNullOrEmpty()) {
                        goToSignup.tryEmit(true)

                    } else {
                        Log.d(TAG, "onCreate: Task Successful")
                        userName.value = it.result.getString("name")!!
                        userEmail.value = it.result.getString("email")!!
                        userProfileImage.value = it.result.getString("profile")!!
                    }
                } else {
                    goToSignup.tryEmit(true)
                }
            }
    }
}