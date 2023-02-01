package dev.jay.gossip.ui.gossip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ListItemOtherMessageBinding
import dev.jay.gossip.databinding.ListItemSelfMessageBinding
import dev.jay.gossip.documents.Message
import dev.jay.gossip.toFormattedTime

private const val TAG = "GossipAdapter"

class MessageDiff : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}
class GossipAdapter : ListAdapter<Message, GossipAdapter.BaseViewHolder>(MessageDiff()) {

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(message: Message)
    }

    class OtherMessageViewHolder(private val binding: ListItemOtherMessageBinding) : BaseViewHolder(binding.root) {
        override fun bind(message: Message) {
            with(binding) {
                textViewUsername.text = message.name
                textViewMessage.text = message.message
                textViewTime.text = message.time.toFormattedTime()
            }
        }
    }

    class SelfMessageViewHolder(private val binding: ListItemSelfMessageBinding) : BaseViewHolder(binding.root) {
        override fun bind(message: Message) {
            with(binding) {
                textViewMessage.text = message.message
                textViewTime.text = message.time.toFormattedTime()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            0 -> {
                val binding = ListItemSelfMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SelfMessageViewHolder(binding)
            }

            else -> {
                val binding = ListItemOtherMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OtherMessageViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).uid) {
            Firebase.auth.currentUser?.uid -> 0
            else -> 1
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }
}