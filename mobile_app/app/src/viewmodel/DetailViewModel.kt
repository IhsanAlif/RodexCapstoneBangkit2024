package com.alice.rodexapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alice.rodexapp.pref.UserRepository
import com.alice.rodexapp.utils.Result
import kotlinx.coroutines.launch

data class InspectionData(
    val inspector: String,
    val roadName: String,
    val roadLength: Int,
    val roadSection: Int,
    val roadSurface: String
)

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _inspectionData = MutableLiveData<Result<InspectionData>>()
    val inspectionData: LiveData<Result<InspectionData>> get() = _inspectionData

    fun getDetailStory(id: String) = userRepository.getDetailStory(id)

    fun saveInspectionData(inspector: String, roadName: String, roadLength: Int, roadSection: Int, roadSurface: String) {
        val data = InspectionData(inspector, roadName, roadLength, roadSection, roadSurface)
        // Simpan data ke repository atau lakukan operasi yang diperlukan
        _inspectionData.value = Result.Success(data)
    }

    fun getInspectionData() {
        viewModelScope.launch {
            // Dapatkan data dari repository atau sumber lain jika diperlukan
            val data = InspectionData("","",0,0,"")
            _inspectionData.value = Result.Success(data)
        }
    }
}
