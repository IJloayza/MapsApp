package com.example.mapsapp.retrofit.models

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.Job

// API Nominatim
class MapViewModel : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> get() = _searchText

    var suggestions = mutableStateListOf<Lugar>()
        private set

    private var searchJob: Job? = null

    init {
        // Lanzamos un colector para aplicar debounce
        viewModelScope.launch {
            _searchText
                .debounce(500) // Espera 500 ms desde el último cambio
                .filter { it.length > 2 }
                .distinctUntilChanged()
                .collectLatest { query ->
                    lookLocations(query)
                }
        }
    }

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
        if (newText.length <= 2) {
            suggestions.clear()
        }
    }

    private suspend fun lookLocations(location: String) {
        try {
            val response = RetrofitClient.instance.buscarLugares(location)
            suggestions.clear()
            if (response.isSuccessful) {
                val places = response.body()
                suggestions.addAll(places?.toList() ?: emptyList())
                Log.d("RetrofitNominatim", "Se ha encontrado la lista de lugares: $places")
            } else {
                Log.d("RetrofitNominatim", "${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("MapViewModel", "Error al buscar lugares: ${e.message}")
        }
    }

    fun cleanSugerencies() {
        suggestions.clear()
    }
}