package dev.jay.gossip.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jay.gossip.documents.Gossip
import javax.inject.Inject

sealed class AddGossipEvent {
    object Loading : AddGossipEvent()
    object Success : AddGossipEvent()
    data class Error(val message: String) : AddGossipEvent()
}

@HiltViewModel
class AddViewModel @Inject constructor() : ViewModel() {

    val state = MutableLiveData<AddGossipEvent>()

    fun addGossip(
        creatorName: String,
        gossip: String,
        tags: List<String>,
        time: Long
    ) {
        state.value = AddGossipEvent.Loading
        val data = Gossip(
            creatorName = creatorName,
            gossip = gossip,
            tags = tags,
            time = time
        )

        Firebase
            .firestore
            .collection("gossip")
            .add(data)
            .addOnSuccessListener {
                state.value = AddGossipEvent.Success
            }
            .addOnFailureListener {
                state.value = AddGossipEvent.Error(it.message.toString())
            }
    }
}