package dev.jay.gossip.ui.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentSignupOTPVerificationBinding

private const val TAG = "SignupOTPVerificationFr"
class SignupOTPVerificationFragment : Fragment() {

    private lateinit var binding: FragmentSignupOTPVerificationBinding
    private lateinit var auth: FirebaseAuth
    private val args by navArgs<SignupOTPVerificationFragmentArgs>()
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
                Log.d(TAG, "onViewCreated: fab clicked")
                val credential = PhoneAuthProvider.getCredential(args.varificationId, binding.inputFieldOtp.text.toString())
                Log.d(TAG, "onViewCreated: credential made")
                try {
                    Log.d(TAG, "onViewCreated: entered try block")
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "onViewCreated: successfully signed in")
                                val user = task.result?.user
                            } else {
                                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                    Log.d(TAG, "onViewCreated: not able to sign in")
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
    }
}