package dev.jay.gossip.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentAddGossipBinding
import dev.jay.gossip.databinding.ListItemGossipBinding
import dev.jay.gossip.documents.Gossip
import dev.jay.gossip.documents.Message
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "AddGossipFragment"
@AndroidEntryPoint
class AddGossipFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddGossipBinding
    private lateinit var fireStoreDatabase : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val sharedViewModel: HomeViewModel by activityViewModels()


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
                val myCalender = Calendar.getInstance()
                val time = SimpleDateFormat("hh:mm a dd-MM-yyyy", Locale.getDefault())
                    .format(myCalender.timeInMillis)
                    .toString()
                val gossip = Gossip(
                    gossipId = Calendar.getInstance().timeInMillis.toString(),
                    creatorUid = auth.uid!!,
                    creatorName = "Name from user collection",
                    gossip = binding.addGossip.editText?.text.toString(),
                    tags =  listOf("Android, Dev"),
                    time = time
                )
                fireStoreDatabase.collection("gossip")
                    .add(gossip)
                    .addOnSuccessListener {
                        sharedViewModel.reFetchAllGossip.value = true
                        Snackbar.make(binding.root,"Gossip added", Snackbar.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "onViewCreated: $it")
                        findNavController().popBackStack()
                    }
            }
        }
    }
}