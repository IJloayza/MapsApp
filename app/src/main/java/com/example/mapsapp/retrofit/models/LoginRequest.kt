package com.example.mapsapp.retrofit.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val contrasena: String
)
