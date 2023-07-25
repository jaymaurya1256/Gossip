package dev.jay.gossip.ui.gossip

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentGossipDialogBinding

class GossipDialogFragment : DialogFragment() {

    private val viewModel: GossipViewModel by activityViewModels()
    private lateinit var binding: FragmentGossipDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGossipDialogBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        // Get the screen dimensions in pixels
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val screenWidthPixels = displayMetrics.widthPixels

        // Calculate the percentage values you want to use (e.g., 30% and 70%)
        val scrollViewWidthPercentage = 0.8f

        // Calculate the height for each view
        val dialogWidth = (screenWidthPixels * scrollViewWidthPercentage).toInt()

        // Find your views by their ids
        val scrollView = binding.dialogScrollView

        // Set the calculated height to your views
        scrollView.layoutParams.width = dialogWidth

        // Request a layout update to apply the new dimensions
        scrollView.requestLayout()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gossipText.text = viewModel.gossipTitle.value
        binding.gossipCreator.text = getString(R.string.posted_by).plus(viewModel.gossipCreatorName.value)
        binding.gossipDate.text = viewModel.gossipDate.value
    }
}