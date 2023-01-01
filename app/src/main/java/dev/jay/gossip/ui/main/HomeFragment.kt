package dev.jay.gossip.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.database.User
import dev.jay.gossip.databinding.FragmentHomeBinding
import dev.jay.gossip.ui.login.LoginActivity
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
        binding.addGossip.setOnClickListener {
            Snackbar.make(binding.root, "hi this is a snack bar", Snackbar.LENGTH_SHORT).show()
        }
    }
}