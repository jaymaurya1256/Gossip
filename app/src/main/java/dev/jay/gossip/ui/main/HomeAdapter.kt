package dev.jay.gossip.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ListItemGossipBinding

class HomeAdapter(): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
//    private lateinit var binding : ListItemGossipBinding
    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.list_item_gossip, parent, false)
        //  binding = ListItemGossipBinding.inflate(LayoutInflater.from(parent.context))
        return HomeViewHolder(binding.rootView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }
}