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
import com.foodtracker.databinding.ConfirmationFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import kotlin.random.Random

class ConfirmationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided ViewBinding class to inflate
        // the layout and then return the root view.
        val binding = ConfirmationFragmentBinding.inflate(inflater, container, false)
        // Firebase database instance
        // ViewModel instance
        val viewModel: UserViewModel by activityViewModels()

        val bundle = arguments
        val foodEntry = bundle!!.getString("entry")
        val calories = bundle.getString("calories")
        val username = bundle.getString("username")

        val database = FirebaseDatabase.getInstance().getReference("Users")

        if (username != null) {
            database.child(username).get().addOnSuccessListener {
                val user = mapOf("stayLoggedIn" to true)
                database.child(viewModel.user.value!!).updateChildren(user).addOnSuccessListener {
                }.addOnFailureListener{
                    Log.i("Box is checked; stay logged in", "Box is checked; stay logged in")
                }

                if (it.exists()) {
                    val name = it.child("name").value
                    val goal = it.child("calorieGoal").value
                    Log.i("Login Fragment", "read fields from Firebase: $name | $goal")
                    viewModel.name.postValue(name.toString())
                    viewModel.goal.postValue(goal.toString())
                    Log.i("Login Fragment", "fields in vm: ${viewModel.name.value} or ${viewModel.name.value.toString()}")
                } else {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
        }




        binding.itemInput.text = "$foodEntry\n${calories}cal"

        //instead of using viewModel to get currentCal, we should try to pull from firebase
        val addToGoal = (viewModel.currentCal.value!!.toInt() + calories!!.toInt()).toString()
        viewModel.currentCal.postValue(addToGoal)

        binding.noButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "No selected",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_confirmationFragment_to_audioActivity)
        }
        binding.yesButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Yes selected",
                Toast.LENGTH_SHORT
            ).show()
            //add to database - mohammad
            findNavController().navigate(R.id.action_confirmationFragment_to_socialMediaFragment)
        }

        // Return the root view.
        return binding.root
    }
}