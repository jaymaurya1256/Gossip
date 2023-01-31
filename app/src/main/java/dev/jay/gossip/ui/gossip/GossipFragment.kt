package dev.jay.gossip.ui.gossip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.databinding.FragmentGossipBinding

private const val TAG = "GossipFragment"
@AndroidEntryPoint
class GossipFragment : Fragment() {

    private val args by navArgs<GossipFragmentArgs>()
    private lateinit var fireStoreDatabase: FirebaseFirestore
    private lateinit var binding: FragmentGossipBinding
    private val viewModel: GossipViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireStoreDatabase = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGossipBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = GossipAdapter()
        binding.recyclerView.adapter = adapter
        viewModel.getGossipContent(args.documentName)

        viewModel.gossipTitle.observe(viewLifecycleOwner) {
            binding.gossip.text = it
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                is GossipEvent.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(it.data)
                }
                is GossipEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is GossipEvent.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.addReply.setOnClickListener {
            try {
                val action =
                    GossipFragmentDirections.actionGossipFragmentToMessageFragment(args.documentName)
                findNavController().navigate(action)
            }catch (e: java.lang.Exception){
                Log.d(TAG, "onViewCreated: $e")
            }
        }
    }
}