package dev.jay.gossip.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentHomeBinding

private const val TAG = "HomeFragment"
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.profileImage.observe(viewLifecycleOwner) {
            Log.d(TAG, "onViewCreatedinhomefragment: $it")
            binding.profileImage.load(it) {
                placeholder(R.drawable.ic_baseline_account_circle_24)
                crossfade(true)
                crossfade(1000)
                error(R.drawable.ic_baseline_account_circle_24)
            }
        }

        val adapter = HomeAdapter {
            val directions = HomeFragmentDirections.actionHomeFragmentToGossipFragment(it)
            findNavController().navigate(directions)
        }

        binding.listGossip.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is HomeEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is HomeEvent.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(it.data)
                }
                is HomeEvent.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.addGossip.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addGossipFragment)
        }

        binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userFragment)
        }

        binding.setting.setOnClickListener { }
    }
}