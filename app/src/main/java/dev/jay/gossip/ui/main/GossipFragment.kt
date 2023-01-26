package dev.jay.gossip.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.databinding.FragmentGossipBinding
import dev.jay.gossip.ui.home.HomeViewModel

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
        sharedViewModel.reFetchAllMessage.value = true
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        sharedViewModel.reFetchAllMessage.observe(viewLifecycleOwner) { reFetch ->
            if (reFetch == true){
                Log.d(TAG, "onViewCreated: re-fetch triggered")
                fireStoreDatabase.collection("gossip").document(args.documentName)
                    .collection("messages").get()
                    .addOnSuccessListener { result ->
                        Log.d(TAG, "onViewCreated: ${result.documents}")
                        binding.recyclerView.adapter = GossipAdapter(result.documents)
                    }
                    .addOnFailureListener {
                        Snackbar.make(binding.root,"Failed to fetch data", Snackbar.LENGTH_SHORT).show()
                    }
                sharedViewModel.reFetchAllMessage.value = false
            }
        }

        fireStoreDatabase.collection("gossip").document(args.documentName)
            .get()
            .addOnSuccessListener {
                binding.gossip.text = it.getString("gossip").toString()
            }
            .addOnFailureListener {
                Snackbar.make(binding.root,"Failed to fetch data", Snackbar.LENGTH_SHORT).show()
            }

        binding.addReply.setOnClickListener {
            try {
                val action = GossipFragmentDirections.actionGossipFragmentToMessageFragment(args.documentName)
                findNavController().navigate(action)
            }catch (e: java.lang.Exception){
                Log.d(TAG, "onViewCreated: $e")
            }
        }
    }
}