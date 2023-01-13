package dev.jay.gossip.ui.main.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ActivityMainBinding
import dev.jay.gossip.ui.signup.SignupActivity
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle : ActionBarDrawerToggle

//    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Firebase.auth.currentUser == null) {
            val intent = Intent(this, SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView = binding.navDrawerHomeFragment

        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.my_gossips -> {Snackbar.make(binding.root, "This feature is not implemented yet", Snackbar.LENGTH_SHORT).show() }
                R.id.sign_out -> {Snackbar.make(binding.root, "This feature is not implemented yet", Snackbar.LENGTH_SHORT).show()}
                R.id.delete_account-> {Snackbar.make(binding.root, "This feature is not implemented yet", Snackbar.LENGTH_SHORT).show()}
            }
            true
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}