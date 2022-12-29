package dev.jay.gossip.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentHomeBinding
import dev.jay.gossip.ui.main.activity.MainViewModel

class HomeFragment() : Fragment() {

    private lateinit var user: String
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
        viewModel.userUID.observe(viewLifecycleOwner){
            binding.email.text = auth.currentUser?.email
            binding.uid.text = auth.currentUser?.uid
            binding.phone.text = auth.currentUser?.phoneNumber
        }
    }
}