package com.example.mapsapp.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.retrofit.models.DeleteUser
import com.example.mapsapp.retrofit.models.ResetPasswordRequest
import com.example.mapsapp.retrofit.models.UpdateUserRequest
import com.example.mapsapp.retrofit.models.User
import com.example.mapsapp.retrofit.models.UserCreate
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header

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
                    Log.d("ProfileFragment: ","${response.errorBody()?.string()}")
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
                    Log.d("ProfileFragment: ","${response.errorBody()?.string()}")
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
                    Log.d("ProfileFragment: ","${response.errorBody()?.string()}")
                }
            } catch (e: Exception){
                Log.e("Retrofit", "Fallo en la solicitud: ${e.message}")
            }
        }
    }
}