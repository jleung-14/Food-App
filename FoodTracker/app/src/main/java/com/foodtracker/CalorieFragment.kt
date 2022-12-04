package com.foodtracker

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
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
            findNavController().navigate(CalorieFragmentDirections.actionCalorieFragmentToWelcomeFragment())
            //binding.root.findViewById<EditText>(R.id.goalText)
            var goalString: String = binding.goalText.text.toString()
            Log.i("hehehehehe", goalString)
            viewModel.goal.postValue(goalString)
            goalString = viewModel.goal.value.toString()
            Log.i("hahahahaha", goalString)
        }


        // Return the root view.
        return binding.root
    }
}