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
import com.google.firebase.database.DatabaseReference
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
        lateinit var database : DatabaseReference
        // ViewModel instance
        val viewModel: UserViewModel by activityViewModels()
        // 'user' name String (retrieved from UserViewModel instance)
        val user: String? = viewModel.user.value
        val name: String? = viewModel.name.value
        val calGoal : String? = viewModel.goal.value
        val calCurr : String? = viewModel.currentCal.value

        // setting on-screen text fields
        binding.welcome.text = "Welcome $name"
        binding.ratio.text = "${calCurr.toString()}/${calGoal.toString()}"
        binding.progressBar.progress = calCurr!!.toInt() / calGoal!!.toInt() * 100

        binding.breakfast.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Breakfast selected",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.meal.postValue("Breakfast")

            findNavController().navigate(R.id.action_welcomeFragment_to_audioFragment)
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

            database = FirebaseDatabase.getInstance().getReference("Users")
            val user1 = mapOf("stayLoggedIn" to false)
            database.child(user!!).updateChildren(user1).addOnSuccessListener {
                Log.i("Welcome Fragment", "Logged out")
            }.addOnFailureListener{
                Log.i("Welcome Fragment", "Log out failed")
            }
            findNavController().popBackStack(R.id.mainFragment, false)
        }

        // Return the root view.
        return binding.root
    }
}