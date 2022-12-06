package com.foodtracker

import android.content.Context
//import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.foodtracker.databinding.ConfirmationFragmentBinding
import com.foodtracker.databinding.DummyFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DummyFragment : Fragment() {

    // Firebase authorization instance
    private lateinit var firebaseAuth: FirebaseAuth
    // Binding to XML layout
    private lateinit var binding: DummyFragmentBinding
    // save login info CheckBox
    private val viewModel : UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Use the provided ViewBinding class to inflate the layout.
        binding = DummyFragmentBinding.inflate(inflater, container, false)
        binding.button.setOnClickListener{
            findNavController().navigate(R.id.action_DummyFragment_to_ListFragment)

        }
        binding.button2.setOnClickListener{
            findNavController().navigate(R.id.action_DummyFragment_to_WelcomeFragment)
        }
        return binding.root
    }

}
