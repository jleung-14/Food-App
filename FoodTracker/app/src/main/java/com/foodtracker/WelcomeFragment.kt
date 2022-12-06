package com.foodtracker

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
        var calCurr : String? = viewModel.currentCal.value
        var progressBar: ProgressBar? = null

        // setting on-screen text fields
        progressBar = binding.root.findViewById<ProgressBar>(R.id.progressBar) as ProgressBar

        binding.welcome.text = "Welcome $name"
        binding.ratio.text = "${calCurr.toString()}/${calGoal.toString()}"
        progressBar.visibility = View.VISIBLE
        if(calCurr!!.toInt() > calGoal!!.toInt()) {
            progressBar.progress = 100
            progressBar.progressTintList = ColorStateList.valueOf(Color.RED);
        }
        else {
            progressBar.progress = (calCurr!!.toInt() * 100)/calGoal!!.toInt()
            progressBar.progressTintList = ColorStateList.valueOf(Color.BLUE);
        }
        binding.Meal.setOnClickListener {
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
        binding.reset.setOnClickListener {
            database = FirebaseDatabase.getInstance().getReference("Users")
            val cal = mapOf("currentCal" to 0)
            database.child(user!!).updateChildren(cal).addOnSuccessListener {
            }.addOnFailureListener{
            }
            viewModel.currentCal.postValue("0")
            progressBar.progress = 0
            calCurr = "0"
            binding.ratio.text = "0/${calGoal.toString()}"

        }

        // Return the root view.
        return binding.root
    }
}