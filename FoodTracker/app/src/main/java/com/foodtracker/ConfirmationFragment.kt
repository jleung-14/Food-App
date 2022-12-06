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
        val viewModel : UserViewModel by activityViewModels()

        val bundle = arguments
        val foodEntry = bundle!!.getString("entry")
        val calories = bundle.getString("calories")
        val username = bundle.getString("username")
        var updatedCalories = 0
        var totalFoods : MutableList<String> = ArrayList()
        val database = FirebaseDatabase.getInstance().getReference("Users")

        if (username != null) {
            database.child(username).get().addOnSuccessListener {
                if (it.exists()) {
                    //food here should be the foods field from firebase, which should be an arrayList of strings
                    if (foodEntry != null && calories != null) {
                        if(it.child("foods").value != null) {
                            var food = it.child("foods").value
                            var temp = food as ArrayList<String>
                            Log.i("CHECKING temp", temp.toString())

                            //currentCal from firebase
                            val curr = it.child("currentCal").value.toString().toInt()
                            updatedCalories = curr + calories.toInt()

                            for (i in temp){
                                totalFoods.add(i)
                            }
                            totalFoods.add("$foodEntry $calories cal")
                            Log.i("CHECKING after adding ", totalFoods.toString())
                        }
                        else{
                            Log.i("testing food", foodEntry)
                            totalFoods.add("$foodEntry $calories cal")
                        }
                    }

                } else {
                    Toast.makeText(requireContext(), "Failed1", Toast.LENGTH_SHORT).show()
                }
                //viewModel.currentCal.postValue((viewModel.currentCal.value.toString().toInt() + viewModel.updateCal.value.toString().toInt()).toString())
                Log.i("viewmodel cal count updated", viewModel.currentCal.value.toString())
                var user = mapOf("currentCal" to updatedCalories.toString())
                database.child(username).updateChildren(user).addOnSuccessListener {
                }.addOnFailureListener{
                }
                //DOUBTS ON THIS LINE, MAP APPARENTLY NEEDS STRING, STRING BUT I DONT THINK WE CAN JUST TOSTRING AN ENTIRE ARRAYLIST
                var thing = mapOf("foods" to totalFoods)
                database.child(username).updateChildren(thing).addOnSuccessListener {
                }.addOnFailureListener{
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
            findNavController().navigate(R.id.action_confirmationFragment_to_listFragment)
        }

        // Return the root view.
        return binding.root
    }
}