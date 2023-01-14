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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentMessageBinding
import dev.jay.gossip.documents.Message

private const val TAG = "MessageFragment"
class MessageFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentMessageBinding
    private lateinit var fireStoreDatabase: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private val args by navArgs<MessageFragmentArgs>()
    private val sharedViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStoreDatabase = FirebaseFirestore.getInstance()
        binding = FragmentMessageBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.send.setOnClickListener {
            if (binding.reply.editText?.text?.isNotBlank() == true){
                try {
                    val reply = Message(
                        binding.reply.editText!!.text.toString(),
                        "Will be accessed through user collection",
                        firebaseAuth.uid!!
                    )
                    fireStoreDatabase.collection("gossip")
                        .document(args.documentName)
                        .collection("messages")
                        .add(reply)
                    sharedViewModel.reFetchAllMessage.value = true
                    findNavController().popBackStack()
                }catch (e: Exception) {
                    Log.d(TAG, "onViewCreated: $e")
                    Snackbar.make(binding.root, "Reply not added. Something went wrong.", Snackbar.LENGTH_SHORT).show()
                }
            }else{
                binding.reply.error = "Blank reply is not allowed"
            }
        }
    }
}