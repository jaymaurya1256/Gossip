package dev.jay.gossip.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ActivitySignupBinding
import dev.jay.gossip.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.signup.setOnClickListener {
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
        }

        binding.backToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}