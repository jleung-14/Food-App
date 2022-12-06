package com.foodtracker

import android.content.Context
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
import com.google.firebase.database.FirebaseDatabase

class MainFragment : Fragment() {

    private val viewModel : UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // user login/info fields retrieved from SharedPref (if any)
        var loggedIn : Any? = null
        val sharePref = context?.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
        if (sharePref != null) {
            val email : String? = sharePref.getString("EMAIL_KEY", null)
            val password : String? = sharePref.getString("PASS_KEY", null)
            val user : String? = sharePref.getString("USER_KEY", null)

                if (email != null && password != null && user != null) {
                    viewModel.email.postValue(email)
                    viewModel.user.postValue(user)

                    val database = FirebaseDatabase.getInstance().getReference("Users")
                    database.child(user).get().addOnSuccessListener {

                        if (it.exists()) {
                            loggedIn = it.child("stayLoggedIn").value
                            val name = it.child("name").value
                            val goal = it.child("calorieGoal").value
                            Log.i("Main Fragment", "Successfully pulled name & goal from db")
                            viewModel.name.postValue(name.toString())
                            viewModel.goal.postValue(goal.toString())
                            Log.i("Main Fragment Login stuff", loggedIn.toString())
                            if(loggedIn == true) {
                                Log.i("LoggedInYay", "We are inside the if statement")
                                val firebaseAuth: FirebaseAuth = requireNotNull(FirebaseAuth.getInstance())
                                firebaseAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.i(
                                                "MainFrag",
                                                "successful Firebase authorization using SharedPref"
                                            )
                                            // issue welcome Toast msg

                                            findNavController().navigate(R.id.action_mainFragment_to_welcomeFragment)
                                        } else {
                                            val editor: SharedPreferences.Editor = sharePref.edit()
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
                            else{
                                Log.i("logged in from welcome", "loggedIn is true but no if statement")
                            }
                        }

                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
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