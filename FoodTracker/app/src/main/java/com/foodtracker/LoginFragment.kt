package com.foodtracker

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
//import com.foodtracker.R
import com.foodtracker.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "Login test"
    }

    private lateinit var firebaseAuth: FirebaseAuth

    // Binding to XML layout
    private lateinit var binding: LoginFragmentBinding
    lateinit var saveInfoBox : CheckBox

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Use the provided ViewBinding class to inflate the layout.
        binding = LoginFragmentBinding.inflate(inflater, container, false)

        firebaseAuth = requireNotNull(FirebaseAuth.getInstance())

        binding.login.setOnClickListener { loginUserAccount() }

        // Return the root view.
        return binding.root
    }

    private val viewModel: UserViewModel by activityViewModels()
    private fun loginUserAccount() {
        val email: String = binding.email.text.toString()
        val password: String = binding.password.text.toString()
        // TODO: find an alternative to this!!
        // save user's email & pass
        viewModel.email.postValue(email)
        viewModel.password.postValue(password)
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
        // TODO: checkbox code
        saveInfoBox = binding.root.findViewById(binding.saveLoginInfo.id)
        if (saveInfoBox.isChecked) {
            Log.d(TAG, "checkbox functional :)")
        }

        binding.progressBar.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // split the email input to extract 'user' portion
                    val emailUser = email.split("@")[0]
                    // set UserViewModel's user field
                    viewModel.user.postValue(emailUser)
                    // issue Toast msg
                    Toast.makeText(
                        requireContext(),
                        "Login successful, welcome $emailUser!",
                        Toast.LENGTH_LONG
                    ).show()

                    findNavController().navigate(
                        R.id.action_loginFragment_to_dashboardFragment
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Login failed! Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
