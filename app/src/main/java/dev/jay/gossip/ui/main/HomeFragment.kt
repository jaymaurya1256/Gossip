package dev.jay.gossip.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentHomeBinding
import dev.jay.gossip.ui.signup.SignupActivity

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment() : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStoreDatabase: FirebaseFirestore
    private lateinit var binding : FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        fireStoreDatabase = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Check if user is already logged in or not
        try {
            sharedPreferences = requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            //Set profile image as provided in register fragment
            val imageURI = sharedPreferences.getString("ProfileImage", "")
            Log.d(TAG, "onViewCreated: $imageURI")
            if (imageURI != null) {
                if (imageURI.isNotBlank()) {
                    binding.profileImage.setImageURI(imageURI.toUri())
                }
            }
        }catch (e: Exception) {
            Snackbar.make(binding.root, "No User data were found", Snackbar.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        //Fetch content and provide it to the Recycler View Adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        sharedViewModel.reFetchAllGossip.value = true
        sharedViewModel.reFetchAllGossip.observe(viewLifecycleOwner) { reFetch ->
            if (reFetch == true){
                Log.d(TAG, "onViewCreated: Refetch triggered")
                fireStoreDatabase.collection("gossip").get()
                    .addOnSuccessListener { result ->
                        binding.recyclerView.adapter = HomeAdapter(result.documents){ documentName ->
                            val action = HomeFragmentDirections.actionHomeFragmentToGossipFragment(documentName)
                            findNavController().navigate(action)
                        }
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "onViewCreated: not able to fetch the document")
                    }
                sharedViewModel.reFetchAllGossip.value = false
            }
            //Attach listener with add gossip button to open bottom sheet fragment
        binding.addGossip.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addGossipFragment)
        }
            //Attach listener with profile icon to open users profile
        binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userFragment)
        }
        }
        binding.setting.setOnClickListener {

        }
    }
}