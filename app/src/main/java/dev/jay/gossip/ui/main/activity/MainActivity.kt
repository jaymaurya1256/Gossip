package dev.jay.gossip.ui.main.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dev.jay.gossip.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            val userUid = intent.extras!!.getString("user")
            viewModel.initUserUID(userUid!!)
        }catch (e: Exception) {
            Snackbar.make(binding.root, "Unable to retrieve info of user", Snackbar.LENGTH_SHORT).show()
        }
    }
}