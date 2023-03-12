package dev.jay.gossip.ui.gossip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentMyGossipBinding
import dev.jay.gossip.ui.home.HomeEvent


@AndroidEntryPoint
class MyGossipFragment : Fragment() {
    private lateinit var binding: FragmentMyGossipBinding
    private val viewModel: MyGossipViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyGossipBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMyGossip()
        binding.recyclerViewMyGossip.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                HomeEvent.Loading -> { binding.progressBar.visibility = View.VISIBLE }
                is HomeEvent.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, getString(R.string.error_loading_data), Snackbar.LENGTH_SHORT).show()
                }
                is HomeEvent.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewMyGossip.adapter = MyGossipAdapter(it.data)
                }
            }
        }
    }
}