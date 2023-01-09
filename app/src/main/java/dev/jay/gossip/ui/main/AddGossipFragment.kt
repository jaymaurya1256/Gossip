package dev.jay.gossip.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentAddGossipBinding
import dev.jay.gossip.databinding.ListItemGossipBinding
import kotlinx.coroutines.delay

private const val TAG = "AddGossipFragment"
@AndroidEntryPoint
class AddGossipFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddGossipBinding
    private lateinit var fireStoreDatabase : FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        fireStoreDatabase = FirebaseFirestore.getInstance()
        binding = FragmentAddGossipBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.send.setOnClickListener {
            if (binding.addGossip.editText?.text?.isBlank() == true){
                binding.addGossip.error = "Please add a Gossip"
            }else{
                val gossip = hashMapOf(
                    "Uid" to "${auth.uid}",
                    "Gossip" to binding.addGossip.editText?.text.toString()
                )
                fireStoreDatabase.collection("gossip")
                    .add(gossip)
                    .addOnSuccessListener {
                        Snackbar.make(binding.root,"Gossip added", Snackbar.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "onViewCreated: $it")
                        Snackbar.make(binding.root,"Failed to add your Gossip", Snackbar.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
            }
        }
    }
}