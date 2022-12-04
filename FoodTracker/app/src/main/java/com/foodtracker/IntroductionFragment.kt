package com.foodtracker

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
import com.foodtracker.databinding.IntroductionFragmentBinding
import com.google.firebase.auth.FirebaseAuth


class IntroductionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided ViewBinding class to inflate
        // the layout and then return the root view.
        val binding = IntroductionFragmentBinding.inflate(inflater, container, false)
        // Firebase database instance
        // ViewModel instance
        val viewModel: UserViewModel by activityViewModels()

        var goalString: String = binding.goalText.text.toString()
        Log.i("hehehehehe", goalString)
        viewModel.goal.postValue(goalString)
        goalString = viewModel.goal.value.toString()
        Log.i("hahahahaha", goalString)

        binding.imageButton.setOnClickListener {
            findNavController().navigate(IntroductionFragmentDirections.actionIntroductionFragmentToWelcomeFragment())
        }
        // Return the root view.
        return binding.root
    }
}