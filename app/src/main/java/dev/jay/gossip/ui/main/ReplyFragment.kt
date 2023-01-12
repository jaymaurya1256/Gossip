package dev.jay.gossip.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentReplyBinding

private const val TAG = "ReplyFragment"
class ReplyFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentReplyBinding
    private lateinit var fireStoreDatabase: FirebaseFirestore
    private val args by navArgs<ReplyFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fireStoreDatabase = FirebaseFirestore.getInstance()
        binding = FragmentReplyBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.send.setOnClickListener {
            if (binding.reply.editText?.text?.isNotBlank() == true){
                try {
                    val reply = hashMapOf(
                        "Reply" to binding.reply.editText!!.text.toString(),
                        "Name" to "Will be accessed through user collection",
                        "Uid" to "Will be accessed through user collection"
                    )
                    fireStoreDatabase.collection("gossip")
                        .document(args.documentName)
                        .collection("messages")
                        .add(reply)
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