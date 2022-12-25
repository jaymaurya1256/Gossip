package dev.jay.gossip.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ActivityLoginBinding
import dev.jay.gossip.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}