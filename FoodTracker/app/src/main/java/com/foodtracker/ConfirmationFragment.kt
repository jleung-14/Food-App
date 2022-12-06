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
import com.foodtracker.databinding.ConfirmationFragmentBinding
import com.google.firebase.database.FirebaseDatabase

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

        val foodEntry = viewModel.meal.value.toString()
        val calories = viewModel.updateCal.value.toString()
        val username = viewModel.user.value.toString()
        var updatedCalories = 0
        val totalFoods : MutableList<String> = ArrayList()
        val database = FirebaseDatabase.getInstance().getReference("Users")

        binding.itemInput.text = "$foodEntry\n${calories}cal"

        binding.noButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "No selected",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_confirmationFragment_to_audioFragment)
        }
        binding.yesButton.setOnClickListener {
            if (username != null) {
                database.child(username).get().addOnSuccessListener {
                    if (it.exists()) {
                        //food here should be the foods field from firebase, which should be an arrayList of strings
                        if(it.child("foods").value != null) {
                            val food = it.child("foods").value
                            val temp = food as ArrayList<String>
                            Log.i("CHECKING temp", temp.toString())
                            for (i in temp){
                                totalFoods.add(i)
                            }
                            totalFoods.add(foodEntry)
                            Log.i("CHECKING after adding ", totalFoods.toString())
                        }
                        else{
                            Log.i("testing food", foodEntry)
                            totalFoods.add(foodEntry)
                        }
                        // currentCal from firebase
                        val curr = it.child("currentCal").value.toString().toInt()

                        updatedCalories = curr + calories.toInt()

                    } else {
                        Toast.makeText(requireContext(), "Failed1", Toast.LENGTH_SHORT).show()
                    }
                    viewModel.currentCal.postValue((viewModel.currentCal.value.toString().toInt() + viewModel.updateCal.value.toString().toInt()).toString())
                    Log.i("viewModel cal count updated", viewModel.currentCal.value.toString())
                    val user = mapOf("currentCal" to updatedCalories.toString())
                    database.child(username).updateChildren(user).addOnSuccessListener {
                    }.addOnFailureListener{
                    }
                    val thing = mapOf("foods" to totalFoods)
                    database.child(username).updateChildren(thing).addOnSuccessListener {
                    }.addOnFailureListener{ }

                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed2", Toast.LENGTH_SHORT).show()
                }

            }

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