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
import com.foodtracker.databinding.DashboardFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class DashboardFragment : Fragment() {

    companion object {
        private const val TAG = "FoodTrackerTest"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided ViewBinding class to inflate
        // the layout and then return the root view.
        val binding = DashboardFragmentBinding.inflate(inflater, container, false)

        // String var for retrieving value inputted to db
        var dbValue : String? = "x"
        // Firebase database instance
        val database = Firebase.database
        // ViewModel instance
        val viewModel: UserViewModel by activityViewModels()
        // 'user' name String (retrieved from UserViewModel instance)
        val user: String? = viewModel.user.value

        // making a key in the db using the user's name
        val myRef = database.getReference(user!!)
        // setting the value for ^ key to lenny face
        myRef.setValue(1001)

        // adding listener for value, updating v when there's a change
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<Int>()
                binding.textView.text = "$user $value"
                dbValue = value.toString()
                Log.d(TAG, "Value is: $value")
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                dbValue = "fail"
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        // update DB button
        binding.updateDB.setOnClickListener {
            // generating a random 3-digit number as a db "access code" of sorts
            val code = Random.nextInt(100, 999)
            myRef.setValue(code)

//            binding.textView.text = "$user $code"

            // issuing a Toast msg with the new code, username, and retrieved value
            Toast.makeText(
                requireContext(),
                "Msg update ($code) ($user) : $dbValue",
                Toast.LENGTH_SHORT
            ).show()
        }

        // logout button
        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(
                requireContext(),
                "You are now logged out!",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().popBackStack(R.id.mainFragment, false)
        }

        // Return the root view.
        return binding.root
    }
}