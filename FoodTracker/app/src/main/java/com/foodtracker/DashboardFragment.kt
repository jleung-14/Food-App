package com.foodtracker

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.foodtracker.databinding.DashboardFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
//import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class DashboardFragment : Fragment() {

    companion object {
        private const val TAG = "DashboardFrag"
    }
    // ViewModel instance
    private val viewModel : UserViewModel by activityViewModels()
    private var user : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided ViewBinding class to inflate the layout and then return the root view.
        val binding = DashboardFragmentBinding.inflate(inflater, container, false)
        // Firebase database instance
        val database = Firebase.database

        if (viewModel.sharedPrefUsed) {
            user = MainActivity.sharedPref.getString("USER_KEY", null)
            Log.i(TAG, "Username from SharedPref: $user")
        } else {
            user = viewModel.user.value
            Log.i(TAG, "Username from viewModel: $user")
        }

        // making a key in the db using the user's name
        val myRef = database.getReference(user!!)

        // adding listener for value, updating v when there's a change
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<Int>()
                if (value == null)
                    myRef.setValue(101)
                binding.textView.text = "$user $value"
                Log.d(TAG, "Value is: $value")
            }
            override fun onCancelled(error: DatabaseError) {
                // failed to read value
                Log.w("Dashboard/myRef listener", "Failed to read value.", error.toException())
                Toast.makeText(
                    requireContext(),
                    "There was an error retrieving your database key :(",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        // update DB button
        binding.updateDB.setOnClickListener {
            // generating a random 3-digit number
            val code = Random.nextInt(102, 999)
            // setting db value to code
            myRef.setValue(code)
        }

        // logout button
        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // reset viewModel's boolean field; applies when sharedPref was used to login, user logs
            // out, and then immediately logs into another account w/out using sharedPref
            viewModel.sharedPrefUsed = false
            val editor : SharedPreferences.Editor = MainActivity.sharedPref.edit()
            editor.clear()
            editor.apply()
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
