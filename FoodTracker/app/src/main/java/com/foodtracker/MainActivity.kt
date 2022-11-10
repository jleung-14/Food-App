package com.foodtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.foodtracker.databinding.MainActivityBinding
//import com.google.firebase.FirebaseApp
import com.google.firebase.provider.FirebaseInitProvider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase APIs
        FirebaseInitProvider()

        // Use the provided ViewBinding class to inflate the
        // layout and set content view to the root view.
        setContentView(MainActivityBinding.inflate(layoutInflater).root)
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        val viewModel : UserViewModel by activityViewModels()
// TODO
//        outState.putCharArray("as", viewModel.email.toCharArray())
//        super.onSaveInstanceState(outState)
//    }
}
