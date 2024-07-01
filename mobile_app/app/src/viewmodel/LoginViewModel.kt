package com.alice.rodexapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alice.rodexapp.pref.UserRepository
import com.alice.rodexapp.model.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun loginUser(email: String, password: String) = userRepository.loginUser(email, password)

    fun saveSession(userModel: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(userModel)
        }
    }
}