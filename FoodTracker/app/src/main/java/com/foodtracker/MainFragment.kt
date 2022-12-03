package com.foodtracker

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.foodtracker.databinding.MainFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment() {

    private val viewModel : UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // user login/info fields retrieved from SharedPref (if any)
        val email : String? = MainActivity.sharedPref.getString("EMAIL_KEY", null)
        val password : String? = MainActivity.sharedPref.getString("PASS_KEY", null)
        val user : String? = MainActivity.sharedPref.getString("USER_KEY", null)

        if (email != null && password != null && user != null) {
            viewModel.sharedPrefUsed = true
            val firebaseAuth : FirebaseAuth = requireNotNull(FirebaseAuth.getInstance())
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("MainFrag", "successful Firebase authorization using SharedPref")
                    // issue welcome Toast msg
                    Toast.makeText(
                        requireContext(),
                        "Welcome back $user!",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(R.id.action_mainFragment_to_welcomeFragment)
                } else {
                    val editor : SharedPreferences.Editor = MainActivity.sharedPref.edit()
                    editor.clear()
                    editor.apply()
                    // login failure
                    Toast.makeText(
                        requireContext(),
                        "Login failed for some reason! Please try again later.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // Use the provided ViewBinding class to inflate the layout.
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_registrationFragment)
        }
        // Return the root view.
        return binding.root
    }

}
