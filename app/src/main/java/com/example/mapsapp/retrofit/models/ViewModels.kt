package com.example.mapsapp.retrofit.models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.example.mapsapp.retrofit.RetrofitPicotrakeManager
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.getUserByEmail(email)
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d("Retrofit", "User: $user")
                    _user.value = user
                } else {
                    Log.e("Retrofit", "Error: ${response.errorBody()}")
                    _user.value = null
                }
            } catch (e: Exception) {
                Log.e("Retrofit", "Error solicitud: ${e.message}")
                _user.value = null
            }
        }
    }

    fun postUser(userCreate: UserCreate){
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.createUser(userCreate)
                if (response.isSuccessful) {
                    Log.d("Retrofit", "User: ")
                } else {
                    Log.e("Retrofit", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("Retrofit", "Error solicitud: ${e.message}")
                _user.value = null
            }
        }
    }

    fun deleteUser(token: String, userToDelete: DeleteUser){
        viewModelScope.launch {
            try {

                val response = RetrofitPicotrakeManager.instance.deleteUser(token, userToDelete)
                if (response.isSuccessful) {
                    Log.d("Retrofit", "User eliminado")
                } else {
                    Log.e("Retrofit", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("Retrofit", "Error solicitud: ${e.message}")
            }
        }
    }

    fun updatePassword(token: String, request: UpdatePasswordRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.updatePassword(token, request)
                if (response.isSuccessful) {
                    Log.d("Retrofit", "La password ha sido actualizada")
                } else {
                    Log.e("Retrofit", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception){
                Log.e("Retrofit", "Error solicitud: ${e.message}")
            }
        }
    }

    fun resetPassword(request: ResetPasswordRequest){
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.resetPassword(request)
                if (response.isSuccessful) {
                    Log.d("Retrofit", "La password ha sido reiniciada")
                } else {
                    Log.e("Retrofit", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception){
                Log.e("Retrofit", "Error solicitud: ${e.message}")
            }
        }
    }

    fun loginUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.loginUser(loginRequest)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("Retrofit", "Login Response: $loginResponse")
                    val token = loginResponse?.access_token
                    if(token != null){
                        val jwt = JWT(token)
                        val userId = jwt.getClaim("sub").asInt()
                        Log.d("Retrofit", "User ID: $userId")
                        if(userId != null){
                            val sharedPref = getApplication<Application>()
                                .getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putInt("userId", userId)
                                //Mirar el import en caso de error putString de tipo Secure
                                putString("accessToken", token)
                                apply()
                            }
                        }
                    }
                } else {
                    Log.e("Retrofit", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("Retrofit", "Exception: ${e.message}")
            }
        }
    }
}