package com.foodtracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.foodtracker.databinding.CalorieFragmentBinding
import com.google.firebase.database.FirebaseDatabase

class CalorieFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CalorieFragmentBinding.inflate(inflater, container, false)
        val viewModel: UserViewModel by activityViewModels()

        binding.imageButton.setOnClickListener {
            // get 'goalText' input
            var cal : String = binding.goalText.text.toString()
            Log.i("Calorie Fragment", "Calories from user: $cal")

            // post goal to db and viewModel's goal field
            viewModel.goal.postValue(cal)
            val database = FirebaseDatabase.getInstance().getReference("Users")
            val user = mapOf("calorieGoal" to cal)
            database.child(viewModel.user.value!!).updateChildren(user).addOnSuccessListener {
                Log.i("Calorie Fragment", "Updated goal in db")
            }.addOnFailureListener{
                Log.i("Calorie Fragment", "Failed to update goal in db")
            }

            //
            val temp = cal
            cal = viewModel.goal.value.toString()
            if (cal == temp)
                Log.i("Calorie Fragment", "calories successfully posted to viewModel")
            else
                Log.w("Calorie Fragment", "WRONG calories from viewModel: $cal")
            findNavController().navigate(CalorieFragmentDirections.actionCalorieFragmentToWelcomeFragment())
        }

        // Return the root view.
        return binding.root
    }
}