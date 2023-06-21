package dev.jay.gossip.ui.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import coil.load
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentUserBinding
import dev.jay.gossip.ui.home.HomeViewModel

private const val TAG = "UserFragment"
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private val viewModel: UserViewModel by viewModels()

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

        binding.progressBar.visibility = View.VISIBLE

        viewModel.userName.observe(viewLifecycleOwner) {
            binding.name.text = it
        }
        viewModel.userCountry.observe(viewLifecycleOwner) {
            binding.nationality.text = it
        }
        viewModel.userDateOfBirth.observe(viewLifecycleOwner) {
            binding.dataOfBirth.text = it
        }
        viewModel.userBio.observe(viewLifecycleOwner) {
            binding.bioText.text = it
        }
        viewModel.userPhone.observe(viewLifecycleOwner) {
            binding.phone.text = it
        }
        viewModel.userEmail.observe(viewLifecycleOwner) {
            binding.email.text = it
        }
        viewModel.profileImage.observe(viewLifecycleOwner) {
//            binding.profileImage.load(it.toUri()) {
//                placeholder(R.drawable.ic_baseline_account_circle_24)
//                error(R.drawable.ic_baseline_account_circle_24)
//            }
            binding.progressBar.visibility = View.GONE
        }
    }
}