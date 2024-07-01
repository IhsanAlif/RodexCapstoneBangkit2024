package com.alice.rodexapp.viewmodel

import androidx.lifecycle.ViewModel
import com.alice.rodexapp.pref.UserRepository
import java.io.File

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
    fun uploadImageWithLocation(
        imageFile: File,
        description: String,
        latitude: Double,
        longitude: Double
    ) = repository.uploadImageWithLocation(imageFile, description, latitude, longitude)
}