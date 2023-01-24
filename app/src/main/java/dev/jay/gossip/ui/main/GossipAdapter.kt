package dev.jay.gossip.ui.main

import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ListItemMessageBinding
import org.w3c.dom.Text

private const val TAG = "GossipAdapter"
class GossipAdapter(private val listOfDocuments: List<DocumentSnapshot>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var binding: ListItemMessageBinding
    private lateinit var fireStoreDatabase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    class GossipViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ListItemMessageBinding.inflate(LayoutInflater.from(parent.context))
        auth = FirebaseAuth.getInstance()
        fireStoreDatabase = FirebaseFirestore.getInstance()
        return GossipViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userUid = listOfDocuments[position].getString("uid")
        if (userUid == auth.uid.toString()){
            binding.myMessageCardView.visibility = View.VISIBLE
            binding.myMessage.text = listOfDocuments[position].getString("message")
        } else {
            binding.otherMessageCardView.visibility = View.VISIBLE
            binding.userName.visibility = View.VISIBLE
            binding.userName.text = listOfDocuments[position].getString("name")
            binding.otherMessage.text = listOfDocuments[position].getString("message")
        }
    }

    override fun getItemCount(): Int {
        return listOfDocuments.size
    }
}