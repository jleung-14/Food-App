package com.foodtracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

        val bundle = arguments
        val foodEntry = bundle!!.getString("entry")
        val calories = bundle.getString("calories")
        val username = bundle.getString("username")
        var totalCalories = 0
        val database = FirebaseDatabase.getInstance().getReference("Users")

        if (username != null) {
            database.child(username).get().addOnSuccessListener {
                if (it.exists()) {
                    //currentCal from firebase
                    val curr = it.child("currentCal").value.toString().toInt()
                    if (calories != null) {
                        totalCalories = curr + calories.toInt()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed1", Toast.LENGTH_SHORT).show()
                }
                val user = mapOf("currentCal" to totalCalories.toString())

                database.child(username).updateChildren(user).addOnSuccessListener {
                }.addOnFailureListener{
                    Log.i("Box is checked; stay logged in", "Box is checked; stay logged in")
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed2", Toast.LENGTH_SHORT).show()
            }
        }



        binding.itemInput.text = "$foodEntry\n${calories}cal"

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