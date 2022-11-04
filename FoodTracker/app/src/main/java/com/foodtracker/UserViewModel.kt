package com.foodtracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    // Current user's name
    var user : MutableLiveData<String> = MutableLiveData<String>()
    var email : MutableLiveData<String> = MutableLiveData<String>()
    var password : MutableLiveData<String> = MutableLiveData<String>()

}