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
    val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided ViewBinding class to inflate
        // the layout and then return the root view.
        val binding = ConfirmationFragmentBinding.inflate(inflater, container, false)


//        binding.mic.setOnClickListener { onRecordButtonClick() }

        binding.button.setOnClickListener { onYesClicked() }
        // Return the layout root view.
        return binding.root
    }

    private fun onYesClicked() {
        //needs to be implemented
    }
}