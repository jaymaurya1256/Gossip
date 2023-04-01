package dev.jay.gossip.ui.signup

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentRegisterBinding
import dev.jay.gossip.documents.User
import dev.jay.gossip.ui.main.activity.MainActivity
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: SignupViewModel by activityViewModels()
    private lateinit var userProfileImageReference: StorageReference
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStoreDatabase : FirebaseFirestore

    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            viewModel.selectedProfileImage = it.toString()
            binding.profileImage.setImageURI(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        fireStoreDatabase = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        userProfileImageReference = storage.reference.child("UsersProfileImages")
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Check if user is signed in or not
        if (auth.currentUser == null) {
            val intent = Intent(requireActivity(), SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        // Check if the user is previously registered or not
        binding.progressBar.visibility = View.VISIBLE
        Firebase.firestore.collection("users").document(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                if (it.getString("name") != null) {
                    binding.progressBar.visibility = View.GONE
                    try {
                        //Take user to Main Activity
                        goToMainActivity()
                    }catch (e: Exception){
                        Snackbar.make(binding.root, "$e", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
            }
        binding.progressBar.visibility = View.GONE


        //Signup with info provided
        binding.signUp.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE

            val name = binding.fullName.editText?.text.toString()
            val email = binding.email.editText?.text.toString()
            val phone = binding.phone.editText?.text.toString()
            val bio = binding.bio.editText?.text.toString()
            val country = binding.country.editText?.text.toString()

            if (name.isNotBlank()) {
                viewModel.name = name
                viewModel.email = email
                viewModel.phoneNumber = phone
                viewModel.bio = bio
                viewModel.country = country
                try {
                    if (viewModel.selectedProfileImage.isNotEmpty()) {
                        //Store Image to the data storage
                        addProfileImageToFireBaseStorage()
                    }
                } catch (e: Exception) {
                    Snackbar.make(binding.root, "$e", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                binding.fullName.error = "This is a required field"
            }
        }

        //Back button listener
        binding.back.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
            findNavController().navigate(R.id.action_registerFragment_to_signupFragment)
        }

        //Select date of birth
        binding.dataOfBirth.setOnClickListener {
            val myCalender = Calendar.getInstance()
            val datePickerListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    myCalender.set(Calendar.YEAR, year)
                    myCalender.set(Calendar.MONTH, month)
                    myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    viewModel.dateOfBirth =
                        SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(myCalender.time)
                            .toString()
                    binding.dataOfBirth.text = viewModel.dateOfBirth
                }
            val datePicker = DatePickerDialog(
                requireContext(),
                datePickerListener,
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePicker.show()
        }

        //Select profile image
        binding.changeProfileImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    //Go to main activity
    private fun goToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    //Save Profile Image to Database
    private fun addProfileImageToFireBaseStorage() {
        val myProfileImageReference = userProfileImageReference.storage.reference
            .child("UserProfileImage/${auth.currentUser!!.uid}")
        myProfileImageReference.putFile(viewModel.selectedProfileImage.toUri())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    myProfileImageReference.downloadUrl
                        .addOnSuccessListener { uriOfImage ->
                            viewModel.selectedProfileImage = uriOfImage.toString()
                            //Save user info in firebase Database
                            addUserInFireStore()
                        }
                        .addOnFailureListener {
                            Snackbar.make(
                                binding.root,
                                "Something went wrong...Please try again",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                }
            }
    }

    //Save user info in firebase Database
    private fun addUserInFireStore() {
        val user = User(
            name = viewModel.name,
            email = viewModel.email,
            phone = viewModel.phoneNumber,
            bio = viewModel.bio,
            country = viewModel.country,
            dateOfBirth = viewModel.dateOfBirth,
            profile = viewModel.selectedProfileImage,
            uid = auth.currentUser!!.uid
        )

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("name", user.name)
            putString("email", user.email)
            putString("phone", user.phone)
            putString("bio", user.bio)
            putString("country", user.country)
            putString("dateOfBirth", user.dateOfBirth)
            putString("profile", user.profile)
            putString("uid", user.uid)
            apply()
        }

        fireStoreDatabase.collection("users").document(auth.currentUser!!.uid)
            .set(user)
            .addOnSuccessListener {
                // Take user to main activity
                goToMainActivity()
            }
            .addOnFailureListener {
                Snackbar.make(
                    binding.root,
                    "Something went wrong",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }
}
