package dev.jay.gossip.ui.signup

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.database.User
import dev.jay.gossip.databinding.FragmentSignupBinding
import dev.jay.gossip.ui.login.LoginActivity
import dev.jay.gossip.ui.main.activity.MainActivity
import java.util.concurrent.TimeUnit

private const val TAG = "SignupFragment"
@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: SignupViewModel by activityViewModels()


    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val REQ_ONE_TAP = 2
    private var showOneTapUI = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next.setOnClickListener {
            if (
                binding.fullName.editText?.text.toString().isNotBlank()
                && binding.contact.editText?.text.toString().isNotBlank()
                && binding.email.editText?.text.toString().isNotBlank()
            ) {
                Log.d(TAG, "onViewCreated: ${binding.fullName.editText.toString()}")
                viewModel.name = binding.fullName.editText?.text.toString()
                viewModel.phoneNumber = binding.contact.editText?.text.toString()
                viewModel.email = binding.email.editText?.text.toString()
                findNavController().navigate(R.id.action_signupFragment_to_signUpSecondPage)
            }else {
                Snackbar.make(binding.root, "All the fields are required field", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.backToLogin.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
        binding.signInWithGoogle.setOnClickListener {
            signInLauncher.launch(signInIntent)
        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
        .build()


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
        val response = result?.idpResponse
        if (result?.resultCode == RESULT_OK) {
            // Successfully signed in
            val intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            if (response != null){
                Log.d(TAG, "onSignInResult: ${response.error?.errorCode}")
                Snackbar.make(binding.root, "Not able to sign in!...", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}