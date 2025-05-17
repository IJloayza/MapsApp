package com.example.mapsapp.retrofit.models

import com.google.gson.JsonObject

data class Mountain(
    val nombre_montanya: String?,
    val descripcion: String?,
    val dificultad: String?,
    val acampar: Boolean,
    val pernoctar: Boolean,
    val especies_peligrosas: Boolean,
    val geojson: JsonObject?
)