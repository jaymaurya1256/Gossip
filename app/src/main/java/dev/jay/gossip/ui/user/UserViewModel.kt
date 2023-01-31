package dev.jay.gossip.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(): ViewModel() {

    init {
        getUserDetails()
    }

    val profileImage = MutableLiveData("")
    val userName = MutableLiveData("")
    val userPhone = MutableLiveData("")
    val userEmail = MutableLiveData("")
    val userDateOfBirth = MutableLiveData("")
    val userCountry = MutableLiveData("")
    val userBio = MutableLiveData("")
    val userUid = MutableLiveData("")

    private fun getUserDetails() {
        Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    profileImage.value = it.result.getString("profile")
                    userName.value = it.result.getString("name")
                    userPhone.value = it.result.getString("phone")
                    userEmail.value = it.result.getString("email")
                    userDateOfBirth.value = it.result.getString("dateOfBirth")
                    userCountry.value = it.result.getString("country")
                    userBio.value = it.result.getString("bio")
                    userUid.value = it.result.getString("uid")
                }
            }
    }
}