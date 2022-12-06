package com.foodtracker

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.foodtracker.databinding.AudioFragmentBinding
import java.util.*

class AudioFragment : Fragment() {

    // on below line we are creating variables
    // for text view and image view
    private lateinit var outputTV: TextView
    private lateinit var micIV: ImageView

    // on below line we are creating a constant value
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var binding1 : AudioFragmentBinding
    private val viewModel : UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = AudioFragmentBinding.inflate(inflater, container, false)
        binding1 = binding
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_audio)

        // initializing variables of list view with their ids.
        outputTV = binding1.root.findViewById(R.id.idTVOutput)
        micIV = binding1.root.findViewById(R.id.idIVMic)

        // on below line we are adding on click
        // listener for mic image view.
        micIV.setOnClickListener {
            // on below line we are calling speech recognizer intent.
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // on below line we are passing language model
            // and model free form in our intent
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            // on below line we are passing our
            // language as a default language.
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )

            // on below line we are specifying a prompt
            // message as speak to text on below line.
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            // on below line we are specifying a try catch block.
            // in this block we are calling a start activity
            // for result method and passing our result code.
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // on below line we are displaying error message in toast
                Toast
                    .makeText(requireContext(), " " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        val calInput = binding1.calorieText.text.toString()
        binding1.btnGoToFragment.setOnClickListener {
            Log.i("AudioActivity", "posting cal + entry to viewModel")
            viewModel.meal.postValue("dummy string")
            viewModel.updateCal.postValue(calInput)

            if (viewModel.meal.value == null)
                Log.w("Audio Fragment", "${viewModel.meal.value}")


            findNavController().navigate(R.id.action_audioFragment_to_confirmFragment)
        }
    }

    // on below line we are calling on activity result method.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // in this method we are checking request code with our result code
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            var transcription = "test"
            // check if result code is ok
            if (resultCode == RESULT_OK && data != null) {

                // extract the data from our array list
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                // set data to our output text view
                outputTV.text = Objects.requireNonNull(res)[0]
                transcription = Objects.requireNonNull(res)[0]
            }

            val calInput = binding1.root.findViewById<EditText>(R.id.calorieText)
            binding1.btnGoToFragment.setOnClickListener {
                Log.i("AudioFragment", "posting cal + entry to viewModel")
                viewModel.meal.postValue(transcription)
                viewModel.updateCal.postValue(calInput.text.toString())
                findNavController().navigate(R.id.action_audioFragment_to_confirmFragment)
            }
        }
    }

}