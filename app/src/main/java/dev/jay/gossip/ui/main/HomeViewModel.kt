package dev.jay.gossip.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val reFetchAllGossip = MutableLiveData(true)
val reFetchAllMessage = MutableLiveData(true)
}