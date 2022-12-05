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
import com.foodtracker.databinding.WelcomeFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class WelcomeFragment : Fragment() {

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
        var name: String? = viewModel.name.value
        var calGoal : String? = viewModel.goal.value
        var calCurr : String? = viewModel.currentCal.value

        // setting on-screen text fields
        binding.welcome.text = "Welcome $name"
        binding.ratio.text = "${calCurr.toString()}/${calGoal.toString()}"

        binding.breakfast.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Breakfast selected",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.meal.postValue("Breakfast")
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRecordFragment())
        }

        binding.lunch.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Lunch selected",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.meal.postValue("Lunch")
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRecordFragment())
        }

        binding.dinner.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Dinner selected",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.meal.postValue("Dinner")
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRecordFragment())
        }

        binding.other.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Other selected",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.meal.postValue("Other")
            findNavController().navigate(R.id.action_welcomeFragment_to_audioActivity)
        }

        binding.goalButton.setOnClickListener {
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToCalorieFragment())
        }

        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // reset viewModel's boolean field; applies when sharedPref was used to login, user logs
            // out, and then immediately logs into another account w/out using sharedPref
//            viewModel.sharedPrefUsed = false
            val editor: SharedPreferences.Editor? =
                activity?.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)?.edit()
            editor?.clear()
            editor?.apply()
            Toast.makeText(
                requireContext(),
                "You are now logged out!",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().popBackStack(R.id.mainFragment, false)
        }

        // Return the root view.
        return binding.root
    }
}