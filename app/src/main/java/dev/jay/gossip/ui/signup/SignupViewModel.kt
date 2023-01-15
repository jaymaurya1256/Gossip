package dev.jay.gossip.ui.signup

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
) : ViewModel() {
    val verificationId = MutableLiveData<String>()
    var name: String = ""
    var phoneNumber: String =""
    var email: String = ""
    var dateOfBirth = ""
    var bio = ""
    var country = ""
    var selectedProfileImage = ""
}