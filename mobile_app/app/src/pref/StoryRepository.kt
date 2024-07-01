package com.alice.rodexapp.pref

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.alice.rodexapp.config.ApiConfig
import com.alice.rodexapp.config.ApiService
import com.alice.rodexapp.model.UserModel
import com.alice.rodexapp.paging.StoryPaging
import com.alice.rodexapp.response.DetailStoryResponse
import com.alice.rodexapp.response.ErrorResponse
import com.alice.rodexapp.response.ListStoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import com.alice.rodexapp.utils.Result


class UserRepository
private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService, userPreference: UserPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }

    fun registerUser(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.registerUser(name, email, password).message
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

    fun loginUser(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.loginUser(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun saveSession(userModel: UserModel) {
        userPreferences.saveSession(userModel)
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        val user = runBlocking { userPreferences.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPaging(apiService)
            }
        ).liveData
    }

    fun getDetailStory(id: String): LiveData<Result<DetailStoryResponse>> = liveData {
        try {
            val user = runBlocking { userPreferences.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val response = apiService.getDetailStory(id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadImage(file: File, description: String) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        try {
            val user = runBlocking { userPreferences.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse = apiService.uploadImage(multipartBody, requestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getStoriesWithLocation() = liveData {
        emit(Result.Loading)
        try {
            val user = runBlocking { userPreferences.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val mapsResponse = apiService.getStoriesWithLocation()
            emit(Result.Success(mapsResponse.listStory))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

    fun uploadImageWithLocation(
        imageFile: File,
        description: String,
        latitude: Double,
        longitude: Double
    ) = liveData {
        emit(Result.Loading)
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        val latitudeRequestBody = latitude.toString().toRequestBody(MultipartBody.FORM)
        val longitudeRequestBody = longitude.toString().toRequestBody(MultipartBody.FORM)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val user = runBlocking { userPreferences.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse =
                apiService.uploadImageWithLocation(multipartBody, descriptionRequestBody, latitudeRequestBody, longitudeRequestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

}
