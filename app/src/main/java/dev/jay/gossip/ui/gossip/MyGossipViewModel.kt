package dev.jay.gossip.ui.gossip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jay.gossip.documents.Gossip
import dev.jay.gossip.ui.home.HomeEvent
import javax.inject.Inject

@HiltViewModel
class MyGossipViewModel @Inject constructor(): ViewModel() {

    val state = MutableLiveData<HomeEvent>()
    val gossips = mutableListOf<Gossip>()


    fun getMyGossip() {

        state.value = HomeEvent.Loading

        Firebase.firestore.collection("gossip").whereEqualTo("uid", Firebase.auth.currentUser?.uid)
            .addSnapshotListener{ value, error ->

                if (error != null) {
                    state.value = HomeEvent.Error(error.toString())
                    return@addSnapshotListener
                }

                for (doc in value!!) {
                    val gossip = doc.toObject<Gossip>()
                    gossip.id = doc.id
                    gossips.add(gossip)
                }
                state.value = HomeEvent.Success(data = gossips)
            }
    }
}