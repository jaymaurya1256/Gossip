package dev.jay.gossip.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("Name","")
        val email = sharedPreferences.getString("Email","")
        val phone = sharedPreferences.getString("Phone","")
        val bio = sharedPreferences.getString("Bio","")
        val country = sharedPreferences.getString("Country","")
        val dob = sharedPreferences.getString("DOB","")
        binding.name.text = name
        binding.nationality.text = country
        binding.dataOfBirth.text = dob
        binding.bioText.text = bio
        binding.phone.text = phone
        binding.email.text = email
    }
}