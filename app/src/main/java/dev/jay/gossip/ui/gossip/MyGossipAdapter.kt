package dev.jay.gossip.ui.gossip


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.jay.gossip.R
import dev.jay.gossip.databinding.MyGossipItemViewBinding
import dev.jay.gossip.documents.Gossip
import java.text.SimpleDateFormat

class MyGossipAdapter(val onClick: (String) -> Unit) : ListAdapter<Gossip, MyGossipAdapter.ViewHolder>(DiffCallback()) {
    private lateinit var binding: MyGossipItemViewBinding

    class ViewHolder(private val binding: MyGossipItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gossip: Gossip, onClick: (String) -> Unit) {
            binding.title.text = gossip.gossip
            binding.date.text = SimpleDateFormat("dd/MM/yyyy").format(gossip.time)
            binding.cardMyGossip.setOnClickListener { onClick(gossip.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = MyGossipItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<Gossip>() {
        override fun areItemsTheSame(oldItem: Gossip, newItem: Gossip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Gossip, newItem: Gossip): Boolean {
            return oldItem == newItem
        }
    }
}
