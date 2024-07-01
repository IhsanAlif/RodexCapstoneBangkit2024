package com.alice.rodexapp.viewmodel

import androidx.lifecycle.ViewModel
import com.alice.rodexapp.pref.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun registerUser(username: String, email: String, password: String) = userRepository.registerUser(username, email, password)
}