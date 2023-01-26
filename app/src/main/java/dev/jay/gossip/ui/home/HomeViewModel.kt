package dev.jay.gossip.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jay.gossip.documents.Gossip
import javax.inject.Inject

sealed class HomeEvent {
    object Loading : HomeEvent()
    data class Success(val data: List<Gossip>) : HomeEvent()
    data class Error(val message: String) : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    val reFetchAllMessage = MutableLiveData<Boolean>(true) // Useless and stupid

    val state = MutableLiveData<HomeEvent>(HomeEvent.Loading)

    init {
        getGossips()
    }

    private fun getGossips() {
        state.value = HomeEvent.Loading
        Firebase
            .firestore
            .collection("gossips")
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