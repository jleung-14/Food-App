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
import com.foodtracker.databinding.IntroductionFragmentBinding
import com.google.firebase.database.FirebaseDatabase


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

        val user = viewModel.user.value
        binding.welcome.text = "Welcome ${viewModel.name.value}!"

        binding.imageButton.setOnClickListener {
            // get 'goalText' input
            var cal : String = binding.goalText.text.toString()
            Log.i("Calorie Fragment", "Calories from user: $cal")

            // post goal to db and viewModel's goal field
            viewModel.goal.postValue(cal)
            val database = FirebaseDatabase.getInstance().getReference("Users")
            val user = mapOf("calorieGoal" to cal)
            database.child(viewModel.user.value!!).updateChildren(user).addOnSuccessListener {
                Log.i("IntoFrag/Firebase", "Updated goal in db")
            }.addOnFailureListener{
                Log.i("IntoFrag/Firebase", "Failed to update goal in db")
            }

            //
            val temp = cal
            cal = viewModel.goal.value.toString()
            if (cal == temp)
                Log.i("Intro Fragment", "calories successfully posted to viewModel")
            else
                Log.w("Intro Fragment", "WRONG calories from viewModel: $cal")

            findNavController().navigate(IntroductionFragmentDirections.actionIntroductionFragmentToWelcomeFragment())
        }
        // Return the root view.
        return binding.root
    }
}