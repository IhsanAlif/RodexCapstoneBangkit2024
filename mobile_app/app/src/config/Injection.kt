package com.alice.rodexapp.config

import android.content.Context
import com.alice.rodexapp.pref.UserPreferences
import com.alice.rodexapp.pref.UserRepository
import com.alice.rodexapp.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val userPreferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { userPreferences.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, userPreferences)
    }
}