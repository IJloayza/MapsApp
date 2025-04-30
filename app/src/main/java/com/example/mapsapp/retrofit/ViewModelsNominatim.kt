package com.example.mapsapp.retrofit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.retrofit.models.DeleteUser
import com.example.mapsapp.retrofit.models.Lugar
import com.example.mapsapp.retrofit.models.ResetPasswordRequest
import com.example.mapsapp.retrofit.models.UpdateUserRequest
import com.example.mapsapp.retrofit.models.User
import com.example.mapsapp.retrofit.models.UserCreate
import kotlinx.coroutines.launch

//API Picotrake

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getUserByEmail(email: String){
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.getUserByEmail(email)
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d("Retrofit", "El email existe y el user es : $user")
                    _user.value = user
                } else{
                    Log.e("Retrofit", "El email no existe: ${response.errorBody()}")
                    _user.value = null
                }
            } catch (e: Exception){
                Log.e("Retrofit", "Fallo en la solicitud: ${e.message}")
                _user.value = null
            }
        }
    }

    fun createUser(user: UserCreate){
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.createUser(user)
                if (response.isSuccessful) {
                    Log.d("Retrofit", "User Creado: $user")
                } else{
                    Log.e("Retrofit", "User No Creado: ${response.errorBody()}")
                }
            } catch (e: Exception){
                Log.e("Retrofit", "Fallo en la solicitud: ${e.message}")
            }
        }
    }

    fun updateUser(token: String, user: UpdateUserRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.updateUser("Bearer $token", user)
                if (response.isSuccessful) {
                    Log.d("Retrofit", "Token correcto : $token $user")
                } else {
                    Log.d("Retrofit: ","${response.errorBody()?.string()}")
                }
            } catch (e: Exception){
                Log.e("Retrofit", "Fallo en la solicitud: ${e.message}")
            }
        }
    }

    fun updatePassword(token: String, user: UpdateUserRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.updateUser("Bearer $token", user)
                if (response.isSuccessful) {
                    Log.d("Retrofit", "Token correcto : $token $user")
                } else {
                    Log.d("Retrofit: ","${response.errorBody()?.string()}")
                }
            } catch (e: Exception){
                Log.e("Retrofit", "Fallo en la solicitud: ${e.message}")
            }
        }
    }

    suspend fun resetPassword(password: ResetPasswordRequest){

    }

    fun deleteUser(token: String, user: DeleteUser){
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.deleteUser("Bearer $token", user)
                if (response.isSuccessful) {
                    Log.d("Retrofit", "Token correcto : $token $user")
                } else {
                    Log.d("Retrofit: ","${response.errorBody()?.string()}")
                }
            } catch (e: Exception){
                Log.e("Retrofit", "Fallo en la solicitud: ${e.message}")
            }
        }
    }
}

// API Nominatim

class MapViewModel : ViewModel() {

    var searchText by mutableStateOf("")
        private set

    var suggestions = mutableStateListOf<Lugar>()
        private set

    fun onSearchTextChange(newText: String) {
        searchText = newText
        if (newText.length > 2 && !newText.isNullOrEmpty()) {
            lookLocations(newText)
        } else {
            suggestions.clear()
        }
    }

    private fun lookLocations(location: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.buscarLugares(location)
                suggestions.clear()
                if (response.isSuccessful){
                    val places = response.body()
                    suggestions.addAll(places?.toList() ?: suggestions)
                    Log.d("RetrofitNominatim", "Se ha encontrado la lista de lugares: $places")
                }else {
                    Log.d("RetrofitNominatim: ","${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "Error al buscar lugares: ${e.message}")
            }
        }
    }

    fun cleanSugerencies() {
        suggestions.clear()
    }
}

