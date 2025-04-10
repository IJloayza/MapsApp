package com.example.mapsapp.retrofit.models

import kotlinx.serialization.Serializable

@Serializable
data class Ad(
    val idAd: Int? = null,
    val titulo: String? = null,
    val contenido: String? = null,
    val tipo_usuario: String? = null,  // "nuevo" o "ex-premium"
    val fecha_inicio: String? = null,
    val fecha_fin: String? = null,
    val activo: Boolean? = true,
    val id_suscripciones: Int? = null
)