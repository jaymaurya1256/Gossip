package dev.jay.gossip.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.databinding.ActivitySignupBinding

private const val TAG = "SignupActivity"
@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}