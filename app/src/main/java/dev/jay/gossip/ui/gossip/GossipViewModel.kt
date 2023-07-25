package dev.jay.gossip.ui.gossip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jay.gossip.documents.Message
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TAG = "GossipViewModel"

sealed class GossipEvent {
    object Loading : GossipEvent()
    data class Success(val data: List<Message>) : GossipEvent()
    data class Error(val message: String) : GossipEvent()
}

@HiltViewModel
class GossipViewModel @Inject constructor(): ViewModel() {

    val gossipTitle = MutableLiveData("")
    val gossipCreatorName = MutableLiveData("")
    val gossipDate = MutableLiveData("")
    val state = MutableLiveData<GossipEvent>(GossipEvent.Loading)

    private fun getMessage(document: String) {
        state.value = GossipEvent.Loading
        Firebase
            .firestore
            .collection("gossip")
            .document(document)
            .collection("messages")
            .orderBy("time")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    state.value = GossipEvent.Error(error.message ?: "Unknown error")
                    return@addSnapshotListener
                }
                val messages = mutableListOf<Message>()
                for (doc in value!!) {
                    val messageObj = doc.toObject<Message>()
                    messageObj.id = doc.id
                    messages.add(messageObj)
                }
                state.value = GossipEvent.Success(messages)
            }
    }

    fun getGossipContent(documentName: String) {
        Firebase
            .firestore
            .collection("gossip").document(documentName)
            .get()
            .addOnSuccessListener {
                gossipTitle.value = it.getString("gossip").toString()
                gossipCreatorName.value = it.getString("creatorName").toString()
                val time = it.getLong("time")
                if (time != null) {
                    val date = Date(time)
                    val outputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
                    val formattedTime = outputFormat.format(date)
                    gossipDate.value = formattedTime
                }

                getMessage(documentName)
            }
            .addOnFailureListener {
                gossipTitle.value = "Not Found: Something went wrong!!!"
            }
    }
}