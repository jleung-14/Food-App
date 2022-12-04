package com.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.foodtracker.databinding.SocialMediaFragmentBinding

class SocialMediaFragment : Fragment() {



    /** Binding to XML layout */
    private lateinit var binding: SocialMediaFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Use the provided ViewBinding class to inflate the layout.
        binding = SocialMediaFragmentBinding.inflate(inflater, container, false)


//
//        binding.register.setOnClickListener { registerNewUser() }

        // Return the root view.
        return binding.root
    }
//
//    private fun registerNewUser() {
//        val email: String = binding.email.text.toString()
//        val password: String = binding.password.text.toString()
//
//
//
//        binding.progressBar.visibility = View.VISIBLE
//
//    }
}