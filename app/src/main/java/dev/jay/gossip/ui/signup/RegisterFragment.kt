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
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentRegisterBinding
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
    private lateinit var auth: FirebaseAuth

    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            viewModel.selectedProfileImage = it.toString()
            binding.profileImage.setImageURI(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Signup with info provided
        binding.signUp.setOnClickListener {
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
                    val sharedPreference =
                        requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.apply {
                        putString("Name", viewModel.name)
                        putString("Email", viewModel.email)
                        putString("Phone", viewModel.phoneNumber)
                        putString("Bio", viewModel.bio)
                        putString("Country", viewModel.country)
                        putString("DOB", viewModel.dateOfBirth)

                        // Copy image to internal storage
                        val storedUri = File(context?.filesDir, "profileImage").toUri()

                        copyUri(
                            requireContext(),
                            viewModel.selectedProfileImage.toUri(),
                            storedUri
                        )

                        putString("ProfileImage", storedUri.toString())

                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }.apply()
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
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(myCalender.time)
                            .toString()
                    binding.dataOfBirth.text = viewModel.dateOfBirth
                }
            DatePickerDialog(
                requireContext(),
                datePickerListener,
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //Select profile image
        binding.changeProfileImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun copyUri(context: Context, pathFrom: Uri, pathTo: Uri?) {
        context.contentResolver.openInputStream(pathFrom).use { inputStream: InputStream? ->
            if (pathTo == null || inputStream == null) return
            context.contentResolver.openOutputStream(pathTo).use { out ->
                if (out == null) return
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int
                while (inputStream.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
            }
        }
    }
}