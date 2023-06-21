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
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.Constant
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentAddGossipBinding
import dev.jay.gossip.documents.Gossip
import dev.jay.gossip.text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddGossipFragment : BottomSheetDialogFragment() {
    private val tags: MutableList<String> = mutableListOf()
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
                        tags,
                        Calendar.getInstance().timeInMillis,
                        Firebase.auth.currentUser!!.uid
                    )
                } catch (e: Exception) {
                    Snackbar.make(binding.root, getString(R.string.something_went_wrong_gossip_not_added), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        binding.buttonAddTag.setOnClickListener {
            if (binding.addTagChipGroup.visibility == View.VISIBLE) {
                binding.addTagChipGroup.visibility = View.GONE
                binding.buttonAddTag.text = getString(R.string.add_tag_or_keyword)
            }
            else{
                binding.addTagChipGroup.visibility = View.VISIBLE
                binding.buttonAddTag.text = getString(R.string.hide_tags)
                binding.addTagChipGroup.removeAllViews()
                for (tag in Constant.TAGS) {
                    val chip = Chip(requireContext())
                    chip.text = tag
                    chip.isClickable = true
                    chip.isCheckable = true
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            tags.plus(chip.text)
                        } else {
                            tags.remove(chip.text)
                        }
                    }
                    binding.addTagChipGroup.addView(chip)
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