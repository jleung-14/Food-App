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
import com.foodtracker.databinding.ListFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ListFragment : Fragment() {
    // Binding to XML layout
    private lateinit var binding: ListFragmentBinding
    private val viewModel : UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Use the provided ViewBinding class to inflate the layout.
        binding = ListFragmentBinding.inflate(inflater, container, false)

        binding.imageButton.setOnClickListener {

            findNavController().navigate(R.id.action_listFragment_to_welcomeFragment)
        }

        // Return the root view.
        return binding.root
    }



}
