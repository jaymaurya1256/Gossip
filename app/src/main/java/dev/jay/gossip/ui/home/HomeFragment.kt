package dev.jay.gossip.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ActivityMainBinding
import dev.jay.gossip.databinding.FragmentHomeBinding
import dev.jay.gossip.databinding.ToolbarMainBinding
import dev.jay.gossip.ui.main.activity.MainActivity

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

        val adapter = HomeAdapter {
            val directions = HomeFragmentDirections.actionHomeFragmentToGossipFragment(it)
            findNavController().navigate(directions)
        }

        //Load the profile image in home fragment
        viewModel.profileImage.observe(viewLifecycleOwner) {
            binding.fragmentHomeProfileImage.load(it.toUri()) {
                placeholder(R.drawable.ic_baseline_account_circle_24)
                error(R.drawable.ic_baseline_account_circle_24)
            }
        }

        //set the adapter of recycler view
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

        binding.fragmentHomeProfileImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userFragment)
        }

        binding.fragmentHomeToggleNavDrawer.setOnClickListener {
            if (requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout).isDrawerOpen(GravityCompat.START)) {
                requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            } else {
                requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }
    }
}
