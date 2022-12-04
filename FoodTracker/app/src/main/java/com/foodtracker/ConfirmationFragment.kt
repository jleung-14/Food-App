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

    companion object {
        private const val TAG = "Dashboard test"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ConfirmationFragmentBinding.inflate(inflater, container, false)
        val viewModel: UserViewModel by activityViewModels()

        binding.noButton.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "No selected",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_confirmationFragment_to_audioActivity)
            //ConfirmationFragmentDirections.actionConfirmationFragmentToAudioActivity()
        }
        binding.yesButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Yes selected",
                Toast.LENGTH_SHORT
            ).show()
            //add to database - mohammad


            findNavController().navigate(R.id.action_confirmationFragment_to_socialMediaFragment)
            //ConfirmationFragmentDirections.actionConfirmationFragmentToSocialMediaFragment()
        }

        // Return the root view.
        return binding.root
    }
}