package dev.jay.gossip.ui.main.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.ActivityMainBinding
import dev.jay.gossip.ui.signup.SignupActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle : ActionBarDrawerToggle

//    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Check if user is logged in or not
        if (Firebase.auth.currentUser == null) {
            val intent = Intent(this, SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

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
                R.id.my_gossips -> {Snackbar.make(binding.root, "This feature is not implemented yet", Snackbar.LENGTH_SHORT).show() }
                R.id.sign_out -> {
                    Firebase.auth.signOut()
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
        val profileImage = getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("ProfileImage","")
        val name = getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("Name","")
        val email = getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("Email","")
        val navDrawerHeader = (binding.navDrawerHomeFragment.getHeaderView(0))
        val profileImageNavDrawer = navDrawerHeader.findViewById<ImageView>(R.id.profile_image)
        val nameNavDrawer = navDrawerHeader.findViewById<TextView>(R.id.name)
        val emailNavHeader = navDrawerHeader.findViewById<TextView>(R.id.email)
        profileImageNavDrawer.setImageURI(profileImage?.toUri())
        nameNavDrawer.text = name
        emailNavHeader.text = email
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}