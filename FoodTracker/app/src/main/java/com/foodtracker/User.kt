package com.foodtracker

data class User(
    val name: String? = null, val user: String? = null,
    val email: String? = null, val calorieGoal: String? = "0",
    val currentCal: String? = "0", val stayLoggedIn: Boolean = false,
    val foods: ArrayList<String> = ArrayList()) { }