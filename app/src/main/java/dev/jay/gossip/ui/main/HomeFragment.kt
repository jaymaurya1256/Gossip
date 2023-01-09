package dev.jay.gossip.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentHomeBinding
import dev.jay.gossip.ui.main.activity.MainActivity
import dev.jay.gossip.ui.main.activity.MainViewModel

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment() : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding : FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
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
        try {
            val sharedPreferences = requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            val name = sharedPreferences.getString("Name", null)
            Snackbar.make(binding.root, "Hi...$name", Snackbar.LENGTH_SHORT).show()
        }catch (e: Exception) {
            Snackbar.make(binding.root, "No User data were found", Snackbar.LENGTH_SHORT).show()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = HomeAdapter()
        binding.addGossip.setOnClickListener {
            Snackbar.make(binding.root, "hi this is a snack bar", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_addGossipFragment)
        }
    }
}