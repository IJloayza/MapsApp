package com.example.mapsapp.retrofit.models

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val nombre_ruta: String? = null,
    val dificultad: String? = null,
    val ubicacion: String? = null,
    val descripcion: String? = null
)

@Serializable
data class UpdateRoute(
    val dificultad: String,
    val descripcion: String
)

