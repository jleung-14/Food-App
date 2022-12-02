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
import com.foodtracker.databinding.RecordFragmentBinding
import com.google.firebase.auth.FirebaseAuth

import kotlin.random.Random

class RecordFragment : Fragment() {

    companion object {
        private const val TAG = "Dashboard test"
    }
    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided ViewBinding class to inflate
        // the layout and then return the root view.
        val binding = RecordFragmentBinding.inflate(inflater, container, false)
        // Firebase database instance
        // ViewModel instance
        // 'user' name String (retrieved from UserViewModel instance)
        val user: String? = viewModel.user.value

        //binding.mic.setOnClickListener { onRecordButtonClick() }
        // Return the layout root view.
        return binding.root
    }

    /*private fun onRecordButtonClick() {
        when {
            viewModel.isPaused -> {
                check(viewModel.isRecording)
                viewModel.resume()
            }
            viewModel.isRecording -> {
                check(!viewModel.isPaused)
                viewModel.pause()
            }
            else -> {
                viewModel.record()
            }
        }
    }*/
}