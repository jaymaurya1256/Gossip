package dev.jay.gossip.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ListItemGossipBinding
import dev.jay.gossip.documents.Gossip

private const val TAG = "HomeAdapter"

class GossipDiff : DiffUtil.ItemCallback<Gossip>() {
    override fun areItemsTheSame(oldItem: Gossip, newItem: Gossip): Boolean {
        return oldItem.gossipId == newItem.gossipId
    }

    override fun areContentsTheSame(oldItem: Gossip, newItem: Gossip): Boolean {
        return oldItem == newItem
    }
}

class HomeAdapter(
    private val listOfDocuments: List<DocumentSnapshot>,
    private val onClick: (String) -> Unit
): ListAdapter<Gossip, HomeAdapter.HomeViewHolder>(GossipDiff()) {
    private lateinit var binding : ListItemGossipBinding
    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_gossip, parent, false)
        binding = ListItemGossipBinding.bind(view)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val documentName = listOfDocuments[position].id
        try {
            binding.textGossipInitiator.text = listOfDocuments[position].getString("gossip")
            binding.dateOfGossip.text = listOfDocuments[position].getString("time")
        }catch (e: Exception){
            Log.d(TAG, "onBindViewHolder: $e")
        }
        binding.root.setOnClickListener {
            onClick(documentName)
        }
    }
}