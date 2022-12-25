package dev.jay.gossip.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}