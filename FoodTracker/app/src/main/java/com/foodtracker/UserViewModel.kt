package com.foodtracker

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel(), DefaultLifecycleObserver {
    // Current user's name
//    var sharedPrefUsed : Boolean = false
    var name: MutableLiveData<String> = MutableLiveData<String>()
    var user : MutableLiveData<String> = MutableLiveData<String>()
    var email : MutableLiveData<String> = MutableLiveData<String>()
    var goal: MutableLiveData<String> = MutableLiveData<String>()
    var meal : MutableLiveData<String> = MutableLiveData<String>()
    var currentCal : MutableLiveData<String> = MutableLiveData<String>()
    var updateCal : MutableLiveData<String> = MutableLiveData<String>()

    // ToDo: is this needed?
    internal fun bindToActivityLifecycle(mainActivity: MainActivity) {
        // Add the current UserViewModel instance as a LifeCycleObserver to the MainActivity
        // using the addObserver function
        mainActivity.lifecycle.addObserver(this)
    }

}
