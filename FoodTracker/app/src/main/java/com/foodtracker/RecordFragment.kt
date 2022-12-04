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
        val binding = RecordFragmentBinding.inflate(inflater, container, false)
        var temp: String? = viewModel.meal.value
        if (temp != null) {
            Log.i("WHEE", temp)
        }
        binding.mealText.text = temp
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