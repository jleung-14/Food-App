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
            // post to viewModel's goal field
            viewModel.goal.postValue(cal)
            cal = viewModel.goal.value.toString()
            Log.i("Calorie Fragment", "Calories from viewModel: $cal")
            findNavController().navigate(CalorieFragmentDirections.actionCalorieFragmentToWelcomeFragment())
        }

        // Return the root view.
        return binding.root
    }
}