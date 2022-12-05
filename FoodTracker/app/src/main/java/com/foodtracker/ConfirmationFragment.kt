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
        binding.itemInput.text = "$foodEntry\n${calories}cal"

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