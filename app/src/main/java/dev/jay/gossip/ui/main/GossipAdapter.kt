package dev.jay.gossip.ui.main

import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ListItemMessageBinding
import dev.jay.gossip.documents.Message
import dev.jay.gossip.ui.home.HomeAdapter
import org.w3c.dom.Text

private const val TAG = "GossipAdapter"

class MessageDiff : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}
class GossipAdapter(
    )
    : ListAdapter<Message, GossipAdapter.GossipViewHolder>(MessageDiff()) {
    private lateinit var fireStoreDatabase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    class GossipViewHolder(val binding: ListItemMessageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GossipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_message, parent, false)
        auth = FirebaseAuth.getInstance()
        fireStoreDatabase = FirebaseFirestore.getInstance()
        return GossipViewHolder(ListItemMessageBinding.bind(view))
    }

    override fun onBindViewHolder(holder: GossipViewHolder, position: Int) {
        val message = getItem(position)
        with(holder.binding) {
            val userUid = message.uid
            if (userUid == auth.uid.toString()) {
                myMessageCardView.visibility = View.VISIBLE
                myMessage.text = message.message
            } else {
                otherMessageCardView.visibility = View.VISIBLE
                userName.visibility = View.VISIBLE
                userName.text = message.name
                otherMessage.text = message.message
            }
        }
    }
}