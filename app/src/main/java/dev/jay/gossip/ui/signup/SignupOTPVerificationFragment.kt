package dev.jay.gossip.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.database.User
import dev.jay.gossip.databinding.FragmentSignupBinding
import dev.jay.gossip.databinding.FragmentSignupOTPVerificationBinding
import dev.jay.gossip.ui.main.activity.MainActivity
import kotlinx.coroutines.launch

private const val TAG = "SignupOTPVerificationFr"
@AndroidEntryPoint
class SignupOTPVerificationFragment : Fragment() {

    private lateinit var binding: FragmentSignupOTPVerificationBinding
    private lateinit var fragmentBinding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: SignupViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        binding = FragmentSignupOTPVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabOtpVerify.setOnClickListener {
            if (binding.inputFieldOtp.text.toString().isNotEmpty()) {
                val credential = PhoneAuthProvider.getCredential(viewModel.verificationId.value!!, binding.inputFieldOtp.text.toString())
                try {
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "onViewCreated: signed in")
                            Snackbar.make(binding.root, "Successfully Signed in", Snackbar.LENGTH_SHORT).show()
                            val user = User(
                                viewModel.name,
                                fragmentBinding.image.toString(),
                                viewModel.phoneNumber,
                                viewModel.email,
                                "Random String Bio",
                                45343453464,
                                "India",
                                "Random Password"
                            )
                            Log.d(TAG, "onViewCreated: user created")
                            viewModel.addUser(user)
                            Log.d(TAG, "onViewCreated: user added")
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            Log.d(TAG, "onViewCreated: intent created : starting a new activity")
                            startActivity(intent)
                        } else {
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(binding.root, "Not able to sign in", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }

                }catch (e: Exception) {
                    Snackbar.make(binding.root, "OTP is wrong, Please Re-Check it thoroughly", Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(binding.root, "Please Enter the OTP", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_signupOTPVerificationFragment_to_signUpSecondPage)
        }
        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_signupOTPVerificationFragment_to_signupFragment)
        }
    }
}