package com.foodtracker

data class User(val name : String? = null, val user : String? = null,
                val email : String? = null, val calorieGoal : String? = null,
                val currentCal : String? = null, val stayLoggedIn: Boolean = false,
                val array: Array<String>? = null) { }