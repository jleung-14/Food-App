package com.foodtracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
//import com.foodtracker.R
import com.foodtracker.databinding.WelcomeFragmentBinding
import com.google.firebase.auth.FirebaseAuth

import kotlin.random.Random

class WelcomeFragment : Fragment() {

    companion object {
        private const val TAG = "Dashboard test"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided ViewBinding class to inflate
        // the layout and then return the root view.
        val binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        // Firebase database instance
        // ViewModel instance
        val viewModel: UserViewModel by activityViewModels()
        // 'user' name String (retrieved from UserViewModel instance)
        val user: String? = viewModel.user.value
        Log.d(TAG, "Username from vm: $user")
        // making a key in the db using the user's name
        // setting the value for ^ key to lenny face

        binding.welcome.text = "Welcome $user"

        // logout button
        binding.breakfast.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Breakfast selected",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.meal.postValue("Breakfast")
        }
        binding.lunch.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Lunch selected",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.meal.postValue("Lunch")
        }
        binding.dinner.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Dinner selected",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.meal.postValue("Dinner")
        }
        binding.other.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Other selected",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.meal.postValue("Other")
        }
        findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRecordFragment()
        )
        // Return the root view.
        return binding.root
    }
}