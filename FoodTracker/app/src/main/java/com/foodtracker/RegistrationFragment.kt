package com.foodtracker

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.foodtracker.databinding.RegistrationFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class RegistrationFragment : Fragment() {

    private var validator = Validators()
    private lateinit var auth: FirebaseAuth
    private lateinit var saveInfoBox : CheckBox
    private val editor : SharedPreferences.Editor = MainActivity.sharedPref.edit()
    private lateinit var database : DatabaseReference
    /** Binding to XML layout */
    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Use the provided ViewBinding class to inflate the layout.
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)

        auth = requireNotNull(FirebaseAuth.getInstance())

        binding.register.setOnClickListener { registerNewUser() }

        // Return the root view.
        return binding.root
    }

    private fun registerNewUser() {
        val name: String = binding.name.text.toString()
        Log.i("RegistrationFrag", name)
        val email: String = binding.email.text.toString()
        val password: String = binding.password.text.toString()

        if (!validator.validEmail(email)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_email),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (!validator.validPassword(password)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_password),
                Toast.LENGTH_LONG
            ).show()

            return
        }
        // Firebase.database

        binding.progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            binding.progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                // generate user field based on email
                var user : String = email.split("@")[0]
                Log.d("Registration Frag", "initial user: $user")
                if (user.any(setOf('.', '#', '$', '[', ']')::contains)) {
                    var c = 0
                    for (x in user) {
                        if (x == '.')
                            user = user.substring(0, c) + '_' + user.substring(c+1)
                        c++
                    }
                    Log.i("Registration Frag", "fixed user: $user")
                }

                // save login info to either sharedPref or viewModel
                saveInfoBox = binding.root.findViewById(binding.saveLoginInfo.id)
                val viewModel : UserViewModel by activityViewModels()
                if (saveInfoBox.isChecked) {
                    viewModel.sharedPrefUsed = true
                    // to SharedPref if checkbox is checked
                    editor.putString("EMAIL_KEY", email)
                    editor.putString("PASS_KEY", password)
                    editor.putString("USER_KEY", user)
                    editor.putString("NAME_KEY", name)
                    editor.apply()
                } else {
                    // otherwise, post valid login info to UserViewModel
                    viewModel.email.postValue(email)
                    viewModel.password.postValue(password)
                    viewModel.user.postValue(user)
                    viewModel.name.postValue(name)
                }

                //
                database = FirebaseDatabase.getInstance().getReference("Users")
                val newUser = User(name, user, email, "0")
                database.child(user).setValue(newUser).addOnSuccessListener {
//                    binding.name.text?.clear()
//                    binding.email.text?.clear()
//                    binding.password.text?.clear()
                    // name, calories,

                    Log.i("Registration Frag", "db child successfully added")

                }.addOnFailureListener{
                    Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
                }

                // issue successful registration Toast msg
                Toast.makeText(
                    requireContext(),
                    getString(R.string.register_success_string),
                    Toast.LENGTH_LONG
                ).show()

                // navigate to dashboard frag
                findNavController().navigate(R.id.action_registrationFragment_to_introductionFragment)
            } else {
                Log.w("RegistrationFrag", "createUserWithEmail:failure", task.exception)
                // issue registration exception Toast msg
                Toast.makeText(
                    requireContext(),
                    task.exception.toString().substringAfter(':').substringBefore('.').trim(),
                    Toast.LENGTH_LONG).
                show()
            }
        }
    }

}