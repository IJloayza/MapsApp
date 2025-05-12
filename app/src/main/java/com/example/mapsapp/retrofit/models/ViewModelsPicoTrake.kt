package com.example.mapsapp.retrofit.models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.example.mapsapp.retrofit.RetrofitPicotrakeManager
import kotlinx.coroutines.launch
//Necesario AndroidViewModel porque necesito usar sharedPrefrences para guardar el token en local
class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user
    private val _suscription = MutableLiveData<SuscriptionType?>()
    val suscription: LiveData<SuscriptionType?> get() = _suscription

    fun getUser(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.getUser(id)
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d("UserViewModel", "User: $user")
                    _user.value = user
                } else {
                    Log.e("UserViewModel", "Error: ${response.errorBody()}")
                    _user.value = null
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error solicitud: ${e.message}")
                _user.value = null
            }
        }
    }

    fun createUser(userCreate: UserCreate) {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.createUser(userCreate)
                if (response.isSuccessful) {
                    // Puedes emitir un estado de éxito si necesitas
                    Log.d("UserViewModel", "Usuario creado con éxito")
                } else {
                    Log.e("UserViewModel", "Error al crear usuario: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Excepción al crear usuario: ${e.message}")
            }
        }
    }

    fun deleteUser(token: String, userToDelete: DeleteUser){
        viewModelScope.launch {
            try {

                val response = RetrofitPicotrakeManager.instance.deleteUser(token, userToDelete)
                if (response.isSuccessful) {
                    Log.d("UserViewModel", "User eliminado")
                } else {
                    Log.e("UserViewModel", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error solicitud: ${e.message}")
            }
        }
    }

    fun updatePassword(token: String, request: UpdatePasswordRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.updatePassword(token, request)
                if (response.isSuccessful) {
                    Log.d("UserViewModel", "La password ha sido actualizada")
                } else {
                    Log.e("UserViewModel", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception){
                Log.e("UserViewModel", "Error solicitud: ${e.message}")
            }
        }
    }

    fun resetPassword(request: ResetPasswordRequest){
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.resetPassword(request)
                if (response.isSuccessful) {
                    Log.d("UserViewModel", "La password ha sido reiniciada")
                } else {
                    Log.e("UserViewModel", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception){
                Log.e("UserViewModel", "Error solicitud: ${e.message}")
            }
        }
    }

    fun getUserSuscription(token: String){
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.getUserSuscription(token)
                if (response.isSuccessful) {
                    _suscription.value = response.body()
                    Log.d("UserViewModel", "Tipo de suscripción: ${response.body()}")
                } else {
                    Log.e("UserViewModel", "Error: ${response.errorBody()}")
                }
            }catch (e: Exception){
                Log.e("UserViewModel", "Error solicitud: ${e.message}")
            }
        }
    }

    fun login(loginRequest: LoginRequest, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.loginUser(loginRequest)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.access_token
                    if (token != null) {
                        val jwt = JWT(token)
                        val userId = jwt.getClaim("sub").asInt()
                        if (userId != null) {
                            val sharedPref = getApplication<Application>()
                                .getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putInt("userId", userId)
                                putInt("suscriptionId", userId)
                                putString("accessToken", token)
                                apply()
                            }
                            onResult(true)
                        } else {
                            Log.e("UserViewModel", "User ID es null")
                            onResult(false)
                        }
                    } else {
                        Log.e("UserViewModel", "Token JWT es null")
                        onResult(false)
                    }
                } else {
                    Log.e("UserViewModel", "Error: ${response.errorBody()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception: ${e.message}")
                onResult(false)
            }
        }
    }
}

class MountainViewModel : ViewModel() {

    private val _mountains = mutableStateOf<List<Mountain>>(emptyList())
    val mountains: State<List<Mountain>> = _mountains

    //Siempre que creo un MountainViewModel ejecutará getMountains
    init {
        getMountains()
    }

    private fun getMountains() {
        viewModelScope.launch {
            try {
                val response = RetrofitPicotrakeManager.instance.getMountains()
                if (response.isSuccessful) {
                    _mountains.value = response.body() ?: emptyList()
                } else {
                    Log.e("MountainViewModel", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MountainViewModel", "Exception: ${e.message}")
            }
        }
    }
}