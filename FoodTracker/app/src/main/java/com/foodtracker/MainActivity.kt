package com.foodtracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foodtracker.databinding.MainActivityBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.provider.FirebaseInitProvider

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var sharedPref : SharedPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enabling offline capability; https://firebase.google.com/docs/database/android/offline-capabilities
        Firebase.database.setPersistenceEnabled(true)
        // Initialize Firebase APIs
        FirebaseInitProvider()

        // Use the provided ViewBinding class to inflate the
        // layout and set content view to the root view.
        setContentView(MainActivityBinding.inflate(layoutInflater).root)

        // getting the data which is stored in shared preferences
        sharedPref = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
    }

}
