package com.alice.rodexapp.viewmodel

import androidx.lifecycle.ViewModel
import com.alice.rodexapp.pref.UserRepository

class MapsViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getStoriesWithLocation() = userRepository.getStoriesWithLocation()
}