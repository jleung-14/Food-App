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
            var goal : Int = binding.goalText.text.toString().toInt()
            Log.i("IntroFrag", "goal input: $goal")

            viewModel.goal.postValue(goal)
            goal = viewModel.goal.value!!.toInt()
            Log.i("IntroFrag", "goal from viewModel: $goal")

            val database = FirebaseDatabase.getInstance().getReference("Users")
            val updateUser = mapOf<String,Any>("calorieGoal" to goal)

            database.child(user!!).updateChildren(updateUser).addOnSuccessListener {
                Log.i("IntroFrag", "Successfully Updated goal")
                Toast.makeText(requireContext(),"New goal set!!",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Log.i("IntroFrag", "FAILed to update goal")
            }

            findNavController().navigate(IntroductionFragmentDirections.actionIntroductionFragmentToWelcomeFragment())
        }
        // Return the root view.
        return binding.root
    }
}