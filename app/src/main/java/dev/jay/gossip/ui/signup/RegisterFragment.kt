package dev.jay.gossip.ui.signup

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import dev.jay.gossip.R
import dev.jay.gossip.databinding.FragmentRegisterBinding
import dev.jay.gossip.ui.main.activity.MainActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUp.setOnClickListener{
            val name = binding.fullName.editText?.text.toString()
            val email = binding.email.editText?.text.toString()
            val phone = binding.phone.editText?.text.toString()
            val bio = binding.bio.editText?.text.toString()
            val country = binding.country.editText?.text.toString()

            if (name.isNotBlank()){
                viewModel.name = name
                viewModel.email = email
                viewModel.phoneNumber = phone
                viewModel.bio = bio
                viewModel.country = country
                try {
                    val sharedPreference =  requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.apply {
                        putString("Name", viewModel.name)
                        putString("Email", viewModel.email)
                        putString("Phone", viewModel.phoneNumber)
                        putString("Bio", viewModel.bio)
                        putString("Country", viewModel.country)
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }.apply()
                }catch (e: Exception) {
                    Snackbar.make(binding.root,"$e",Snackbar.LENGTH_SHORT).show()
                }
            }else{
                binding.fullName.error = "This is a required field"
            }
        }

        binding.back.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
            findNavController().navigate(R.id.action_registerFragment_to_signupFragment)
        }
        binding.dataOfBirth.setOnClickListener {
            val myCalender = Calendar.getInstance()
            val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                myCalender.set(Calendar.YEAR, year)
                myCalender.set(Calendar.MONTH, month)
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.dateOfBirth = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    .format(myCalender.time)
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
    }
}