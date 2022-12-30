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
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentHomeBinding
import dev.jay.gossip.ui.login.LoginActivity
import dev.jay.gossip.ui.main.activity.MainActivity
import dev.jay.gossip.ui.main.activity.MainViewModel

private const val TAG = "HomeFragment"
class HomeFragment() : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding : FragmentHomeBinding

    val viewModel: MainViewModel by activityViewModels()

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
        binding.email.text = auth.currentUser?.email
        binding.uid.text = auth.currentUser?.uid
        binding.phone.text = auth.currentUser?.phoneNumber
        binding.logout.setOnClickListener {
            Log.d(TAG, "onViewCreated: clicked")
            auth.signOut()
            Log.d(TAG, "onViewCreated: signed out")
            Snackbar.make(binding.root, "signed out", Snackbar.LENGTH_SHORT).show()
            Log.d(TAG, "onViewCreated: snackbar shown")
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            Log.d(TAG, "onViewCreated: intent made")
            startActivity(intent)
        }
        binding.addGossip.setOnClickListener {
            Snackbar.make(binding.root, "hi this is a snack bar", Snackbar.LENGTH_SHORT).show()
        }
    }
}