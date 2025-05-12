package com.example.mapsapp.retrofit

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthState {
    AUTHENTICATED,
    EXPIRED,
    UNAUTHENTICATED
}

class AuthTokenJWTViewModel(application: Application) : AndroidViewModel(application) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.UNAUTHENTICATED)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkToken()
    }

    private fun checkToken() {
        val sharedPref = getApplication<Application>().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPref.getString("accessToken", null)

        if (token != null) {
            try {
                val jwt = JWT(token)
                if (!jwt.isExpired(10)) {
                    _authState.value = AuthState.AUTHENTICATED
                } else {
                    _authState.value = AuthState.EXPIRED
                }
            } catch (e: Exception) {
                _authState.value = AuthState.UNAUTHENTICATED
            }
        } else {
            _authState.value = AuthState.UNAUTHENTICATED
        }
    }

    fun logout() {
        val prefs = getApplication<Application>().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        _authState.value = AuthState.UNAUTHENTICATED
    }
}