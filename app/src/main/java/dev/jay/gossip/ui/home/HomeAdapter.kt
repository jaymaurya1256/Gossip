package dev.jay.gossip.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ListItemGossipBinding
import dev.jay.gossip.documents.Gossip

private const val TAG = "HomeAdapter"

class GossipDiff : DiffUtil.ItemCallback<Gossip>() {
    override fun areItemsTheSame(oldItem: Gossip, newItem: Gossip): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Gossip, newItem: Gossip): Boolean {
        return oldItem == newItem
    }
}

class HomeAdapter(
    private val onClick: (String) -> Unit
): ListAdapter<Gossip, HomeAdapter.HomeViewHolder>(GossipDiff()) {

    class HomeViewHolder(val binding: ListItemGossipBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_gossip, parent, false)
        return HomeViewHolder(ListItemGossipBinding.bind(view))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val gossip = getItem(position)
        with (holder.binding) {
            textViewGossipTitle.text = gossip.gossip
            textViewGossipTags.text = gossip.tags.joinToString(", ")
            textViewGossipTime.text = gossip.time.toString()

            root.setOnClickListener {
                onClick(gossip.id)
            }
        }
    }
}