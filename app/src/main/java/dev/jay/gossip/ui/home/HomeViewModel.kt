package dev.jay.gossip.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jay.gossip.documents.Gossip
import javax.inject.Inject

private const val TAG = "HomeViewModel"
sealed class HomeEvent {
    object Loading : HomeEvent()
    data class Success(val data: List<Gossip>) : HomeEvent()
    data class Error(val message: String) : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    val profileImage = MutableLiveData("")
    val state = MutableLiveData<HomeEvent>(HomeEvent.Loading)

    init {
        getGossips()
        getProfileImage()
    }

    private fun getProfileImage() {
        Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid).get()
            .addOnCompleteListener {
            if (it.isSuccessful) {
                profileImage.value = it.result.getString("profile")!!
            }
        }
    }
    private fun getGossips() {
        state.value = HomeEvent.Loading
        Firebase
            .firestore
            .collection("gossip")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    state.value = HomeEvent.Error(error.message ?: "Unknown error")
                    return@addSnapshotListener
                }

                val gossips = mutableListOf<Gossip>()
                for (doc in value!!) {
                    val gossip = doc.toObject<Gossip>()
                    gossip.id = doc.id
                    gossips.add(gossip)
                }
                state.value = HomeEvent.Success(gossips)
            }
    }
}