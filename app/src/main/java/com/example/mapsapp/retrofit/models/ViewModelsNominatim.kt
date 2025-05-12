package com.example.mapsapp.retrofit.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.retrofit.RetrofitClient
import com.example.mapsapp.retrofit.RetrofitPicotrakeManager
import kotlinx.coroutines.launch
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

