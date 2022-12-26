package dev.jay.gossip.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ActivitySignupBinding
import dev.jay.gossip.ui.login.LoginActivity
import java.util.concurrent.TimeUnit

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.signup.setOnClickListener {
            val phoneNumber = binding.contact.text.toString()
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                    }
                }
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    popUpInputCode(verificationId, binding)
                }

            }
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

            /**
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email.isEmpty() || password.isEmpty()){
            Snackbar.make(this,binding.root,"Either email or password is empty", Snackbar.LENGTH_SHORT).show()
            }else{
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
            Snackbar.make(this,binding.root,"User is successfully Created", Snackbar.LENGTH_SHORT).show()
            }else{
            Snackbar.make(this,binding.root,"User not created", Snackbar.LENGTH_SHORT).show()
            }
            }
            }
             **/

        }

        binding.backToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun popUpInputCode(verificationId: String, binding: ActivitySignupBinding) {
        val popupWindow = PopupWindow(this)
        val view = layoutInflater.inflate(R.layout.pop_up_window_phone_otp, binding.root)
        popupWindow.contentView = view
        popupWindow.showAsDropDown(binding.logo)
        view.findViewById<FloatingActionButton>(R.id.fab_otp_verify).setOnClickListener {
            if (view.findViewById<EditText>(R.id.input_field_otp).text.isNullOrEmpty()){
                Snackbar.make(view,"Empty Field",Snackbar.LENGTH_SHORT).show()
            }else{
                val credential = PhoneAuthProvider.getCredential(verificationId!!,
                    view.findViewById<EditText>(R.id.input_field_otp).text.toString())
                signInWithPhoneAuthCredential(credential)
            }
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
    }
}