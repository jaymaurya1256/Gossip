package dev.jay.gossip.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.database.User
import dev.jay.gossip.databinding.FragmentSignupBinding
import dev.jay.gossip.ui.login.LoginActivity
import java.util.concurrent.TimeUnit

private const val TAG = "SignupFragment"
@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: SignupViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next.setOnClickListener {
            if (
                binding.fullName.editText.toString().isNotEmpty()
                && binding.contact.editText.toString().isNotEmpty()
                && binding.email.editText.toString().isNotEmpty()
            ) {
                viewModel.name = binding.fullName.toString()
                viewModel.phoneNumber = binding.contact.toString()
                viewModel.email = binding.email.toString()
                findNavController().navigate(R.id.action_signupFragment_to_signUpSecondPage)
            }else {
                Snackbar.make(binding.root, "All the fields are required field", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.backToLogin.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

}