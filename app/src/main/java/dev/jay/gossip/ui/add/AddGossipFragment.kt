package dev.jay.gossip.ui.add

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentAddGossipBinding
import dev.jay.gossip.documents.Gossip
import dev.jay.gossip.text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddGossipFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddGossipBinding
    private val viewModel by viewModels<AddViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddGossipBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.send.setOnClickListener {
            if (binding.addGossip.text.isEmpty()) {
                binding.addGossip.error = "Please add a Gossip"
            } else {

                binding.addGossip.error = null
                try {
                    viewModel.addGossip(
                        requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString("name", "")!!,
                        binding.addGossip.text,
                        listOf("Android", "Kotlin", "Jetpack"),
                        Calendar.getInstance().timeInMillis,
                        Firebase.auth.currentUser!!.uid
                    )
                } catch (e: Exception) {
                    Snackbar.make(binding.root, getString(R.string.something_went_wrong_gossip_not_added), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is AddGossipEvent.Loading -> {

                }
                is AddGossipEvent.Success -> {
                    Toast.makeText(requireContext(), "Gossip added successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is AddGossipEvent.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}