package dev.jay.gossip.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.database.UserDatabase
import dev.jay.gossip.databinding.FragmentHomeBinding
import dev.jay.gossip.ui.main.activity.MainActivity
import dev.jay.gossip.ui.main.activity.MainViewModel
import dev.jay.gossip.ui.signup.SignupActivity

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment() : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStoreDatabase: FirebaseFirestore
    private lateinit var binding : FragmentHomeBinding
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
            val sharedPreferences = requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        }catch (e: Exception) {
            Snackbar.make(binding.root, "No User data were found", Snackbar.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        //Fetch content and provide it to the Recycler View Adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        sharedViewModel.reFetchAllGossip.value = true
        sharedViewModel.reFetchAllGossip.observe(viewLifecycleOwner) {
            if (it == true){
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
        }
        binding.addGossip.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addGossipFragment)
        }
        binding.profileImage.setOnClickListener {
        }
        binding.setting.setOnClickListener {

        }
    }
}