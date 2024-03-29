package com.foodtracker

import android.content.Context
//import android.content.SharedPreferences
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
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment() {

    // Firebase authorization instance
    private lateinit var firebaseAuth: FirebaseAuth
    // Binding to XML layout
    private lateinit var binding: LoginFragmentBinding
    // save login info CheckBox
    private lateinit var saveInfoBox : CheckBox
    private val viewModel : UserViewModel by activityViewModels()

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
//            viewModel.sharedPrefUsed = true
            Log.i("Login Fragment", "checkbox works, editing sharedPref")
            // below two lines will put values for email and password in shared preferences

            val editor = activity?.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)?.edit()
            editor?.putString("EMAIL_KEY", email)
            editor?.putString("PASS_KEY", password)
            editor?.apply()

        }
        viewModel.email.postValue(email)

        // splicing email to get username
        var user : String = email.split("@")[0]
        if (user.any(setOf('.', '#', '$', '[', ']')::contains)) {
            var c = 0
            for (x in user) {
                if (x == '.')
                    user = user.substring(0, c) + '_' + user.substring(c+1)
                c++
            }
            Log.i("Login Fragment", "fixed username: $user")
        }
        viewModel.user.postValue(user)

        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(user).get().addOnSuccessListener {
            if (saveInfoBox.isChecked) {
                val user = mapOf("stayLoggedIn" to true)
                database.child(viewModel.user.value!!).updateChildren(user).addOnSuccessListener {
                }.addOnFailureListener{
                    Log.i("Box is checked; stay logged in", "Box is checked; stay logged in")
                }
            }

            if (it.exists()) {
                val name = it.child("name").value
                val goal = it.child("calorieGoal").value
                val cal = it.child("currentCal").value
                Log.i("Login Fragment", "fields from Firebase: $name | $goal | $cal")
                viewModel.name.postValue(name.toString())
                viewModel.goal.postValue(goal.toString())
                viewModel.currentCal.postValue(cal.toString())
                Log.i("Login Fragment", "fields in vm: ${viewModel.name.value} | ${viewModel.name.value.toString()}")
            } else {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }

        binding.progressBar.visibility = View.VISIBLE
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            binding.progressBar.visibility = View.GONE

            if (task.isSuccessful) {
                if (saveInfoBox.isChecked) {
                    val editor = activity?.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)?.edit()
                    editor?.putString("USER_KEY", user)
                    editor?.apply()
                } else
                // set viewModel's user field if SharedPref isn't being used
                    viewModel.user.postValue(user)

                findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
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
