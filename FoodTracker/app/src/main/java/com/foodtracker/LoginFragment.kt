package com.foodtracker

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.foodtracker.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "Login test"
    }

    // Firebase authorization instance
    private lateinit var firebaseAuth: FirebaseAuth
    // Binding to XML layout
    private lateinit var binding: LoginFragmentBinding
    // save login info CheckBox
    private lateinit var saveInfoBox : CheckBox

    private val viewModel : UserViewModel by activityViewModels()
    private val editor : SharedPreferences.Editor = MainActivity.sharedPref.edit()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Use the provided ViewBinding class to inflate the layout.
        binding = LoginFragmentBinding.inflate(inflater, container, false)

        firebaseAuth = requireNotNull(FirebaseAuth.getInstance())

        binding.login.setOnClickListener { loginUserAccount() }

        // Return the root view.
        return binding.root
    }

    private fun loginUserAccount() {
        val email: String = binding.email.text.toString().lowercase()
        val password: String = binding.password.text.toString()

        Log.d(TAG, "Username & pass sent to vm: $email & $password")
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.login_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.password_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        saveInfoBox = binding.root.findViewById(binding.saveLoginInfo.id)
        if (saveInfoBox.isChecked) {
            viewModel.sharedPrefUsed = true
            Log.i(TAG, "checkbox works, editing sharedPref")
            // below two lines will put values for email and password in shared preferences
            editor.putString("EMAIL_KEY", email)
            editor.putString("PASS_KEY", password)
            // save our data with key and value
            editor.apply()
        } else {
            viewModel.email.postValue(email)
            viewModel.password.postValue(password)
        }
        binding.progressBar.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            binding.progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                // split the email input to extract 'user' portion
                var user : String = email.split("@")[0]
                Log.d(TAG, "initial user: $user")
                if (user.any(setOf('.', '#', '$', '[', ']')::contains)) {
                    var c = 0
                    for (x in user) {
                        if (x == '.')
                            user = user.substring(0, c) + '_' + user.substring(c+1)
                        c++
                    }
                    Log.i(TAG, "fixed user: $user")
                }
                if (saveInfoBox.isChecked) {
                    editor.putString("USER_KEY", user)
                    editor.apply()
                } else
                // set UserViewModel's user field if SharedPref isn't being used
                    viewModel.user.postValue(user)

                findNavController()
                    .navigate(R.id.action_loginFragment_to_dashboardFragment)
            } else {
                // login failure
                Toast.makeText(
                    requireContext(),
                    "Login failed! Please try again later.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

}
