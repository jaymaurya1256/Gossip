package dev.jay.gossip.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentGossipBinding

private const val TAG = "GossipFragment"
@AndroidEntryPoint
class GossipFragment : Fragment() {

    private val args by navArgs<GossipFragmentArgs>()
    private lateinit var fireStoreDatabase: FirebaseFirestore
    private lateinit var binding: FragmentGossipBinding
    private val sharedViewModel: HomeViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireStoreDatabase = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGossipBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fireStoreDatabase.collection("gossip").document(args.documentName)
            .get()
            .addOnSuccessListener {
                binding.gossip.text = it.getString("Gossip").toString()
            }
            .addOnFailureListener {
                Snackbar.make(binding.root,"Failed to fetch data", Snackbar.LENGTH_SHORT).show()
            }
    }
}