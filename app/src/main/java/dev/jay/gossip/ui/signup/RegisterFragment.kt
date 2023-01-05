package dev.jay.gossip.ui.signup

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentRegisterBinding
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: SignupViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUp.setOnClickListener{
            Log.d(TAG, "onCreate: entered")
            val phoneNumber = viewModel.phoneNumber
            Log.d(TAG, "onCreate: phone number $phoneNumber")
            if (phoneNumber.isNotEmpty()) {
                if (phoneNumber.length == 10) {
                    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            signInWithPhoneAuthCredential(credential)
                        }
                        override fun onVerificationFailed(e: FirebaseException) {
                            if (e is FirebaseAuthInvalidCredentialsException) {
                                // Invalid request
                                Snackbar.make(binding.root, "Invalid request", Snackbar.LENGTH_SHORT).show()
                            } else if (e is FirebaseTooManyRequestsException) {
                                // The SMS quota for the project has been exceeded
                                Snackbar.make(binding.root, "Too Many Request", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            Log.d(TAG, "onCodeSent: entered")
                            viewModel.verificationId.value = verificationId
                        }
                    }
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91$phoneNumber")       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }else{
                    Snackbar.make(binding.root, "Please Enter the Correct number", Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(binding.root, "Please Enter the Contact number", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.back.setOnClickListener {
        }
        binding.dataOfBirth.setOnClickListener {
            val myCalender = Calendar.getInstance()
            val datePickerListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                myCalender.set(Calendar.YEAR, year)
                myCalender.set(Calendar.MONTH, month)
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.dateOfBirth = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).toString()
                binding.dataOfBirth.text = viewModel.dateOfBirth
            }
            DatePickerDialog(
                requireContext(),
                datePickerListener,
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
    }
}