package dev.jay.gossip.ui.main.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ActivityMainBinding
import dev.jay.gossip.ui.signup.SignupActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle : ActionBarDrawerToggle
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.goToSignup.collect {
                gotoSignupActivity()
            }
        }
        viewModel.checkUserRegistered()

        //Create Drawer layout
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView = binding.navDrawerHomeFragment
        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //Bind Nav Drawer Views with shared preferences data
        bindNavDrawer()

        //Implement all the menu item in nav drawer
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.my_gossips -> {
                    findNavController(R.id.nav_host_fragment_container).navigate(R.id.action_global_myGossipsFragment)
                }
                R.id.sign_out -> {
                    Firebase.auth.signOut()
                    Snackbar.make(binding.root,
                        "Signed Out",
                        Snackbar.LENGTH_SHORT).show()
                    val intent = Intent(this, SignupActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                R.id.delete_account-> {Snackbar.make(binding.root, "This feature is not implemented yet", Snackbar.LENGTH_SHORT).show()}
            }
            true
        }
    }

    private fun bindNavDrawer() {
        val navDrawerHeader = (binding.navDrawerHomeFragment.getHeaderView(0))
        val profileImageNavDrawer = navDrawerHeader.findViewById<ImageView>(R.id.profile_image)
        val progressBar = navDrawerHeader.findViewById<ProgressBar>(R.id.progress_bar)
        val nameNavDrawer = navDrawerHeader.findViewById<TextView>(R.id.name)
        val emailNavHeader = navDrawerHeader.findViewById<TextView>(R.id.email)

        progressBar.visibility = View.VISIBLE

        viewModel.userName.observe(this) {
            nameNavDrawer.text = it
        }

        viewModel.userEmail.observe(this) {
            emailNavHeader.text = it
        }

        viewModel.userProfileImage.observe(this) {
            profileImageNavDrawer.load(it.toUri()) {
                Log.d(TAG, "bindNavDrawer:$it")
                placeholder(R.drawable.ic_baseline_account_circle_24)
                error(R.drawable.ic_baseline_account_circle_24)
            }
            progressBar.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun gotoSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        Firebase.auth.signOut()
        this.getSharedPreferences("userDetails", Context.MODE_PRIVATE).edit().clear().apply()
    }
}