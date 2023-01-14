package dev.jay.gossip.ui.main

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ListItemMessageBinding
import org.w3c.dom.Text

private const val TAG = "GossipAdapter"
class GossipAdapter(val listOfDocument: List<DocumentSnapshot>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var binding: ListItemMessageBinding
    private lateinit var auth: FirebaseAuth

    class GossipViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_message, parent, false)
        binding = ListItemMessageBinding.inflate(LayoutInflater.from(parent.context))
        auth = FirebaseAuth.getInstance()
        return GossipViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listOfDocument[position].getString("uid") == auth.uid.toString()){
            binding.myMessage.text = listOfDocument[position].getString("message")
            Log.d(TAG, "onBindViewHolder: myMessage triggered")
            //binding.message.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        } else {
            binding.otherMessage.text = listOfDocument[position].getString("message")
        }
        Log.d(TAG, "onBindViewHolder: ${listOfDocument[position].getString("uid")}")
        Log.d(TAG, "onBindViewHolder: ${auth.uid}")
        Log.d(TAG, "onBindViewHolder: ${listOfDocument[position].getString("message")}")
    }

    override fun getItemCount(): Int {
        return listOfDocument.size
    }
}