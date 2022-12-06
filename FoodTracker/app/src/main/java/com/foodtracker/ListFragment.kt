package com.foodtracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.foodtracker.databinding.ListFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import kotlin.random.Random

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided ViewBinding class to inflate
        // the layout and then return the root view.
        val binding = ListFragmentBinding.inflate(inflater, container, false)
        // Firebase database instance
        val viewModel : UserViewModel by activityViewModels()
        val foodEntry = viewModel.meal.value.toString()
        val calories = viewModel.updateCal.value.toString()
        val username = viewModel.user.value.toString()
        var totalFoods : MutableList<String> = ArrayList()
        val database = FirebaseDatabase.getInstance().getReference("Users")
        var food: Any? = null

        val fireBaseTextView :TextView  = binding.root.findViewById(binding.ItemView.id)
        //Write a message to the database
//            val myRef = database.get();
        database.child(username).get().addOnSuccessListener {
            if (it.exists()) {
                food = it.child("foods").value
                Log.i("Login Fragment", "fields in vm: $food")
                //fireBaseTextView.text = food.toString()
                binding.ItemView.text = food.toString()
            } else {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        }
        Log.i("food.toString()", food.toString())
        binding.imageButton.setOnClickListener {

            findNavController().navigate(R.id.action_listFragment_to_welcomeFragment)
        }
        return binding.root
    }
}