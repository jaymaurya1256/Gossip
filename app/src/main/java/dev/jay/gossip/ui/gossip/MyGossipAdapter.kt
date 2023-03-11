package dev.jay.gossip.ui.gossip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.jay.gossip.R
import dev.jay.gossip.documents.Gossip
import java.text.SimpleDateFormat

class MyGossipAdapter(val list: List<Gossip>) : RecyclerView.Adapter<MyGossipAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.title)
        val date = view.findViewById<TextView>(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_gossip_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].gossip
        holder.date.text = SimpleDateFormat("dd/MM/yyyy").format(list[position].time)
    }

}
