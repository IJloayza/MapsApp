package com.example.mapsapp.retrofit.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id_usuarios: Int,
    val nombre: String? = null,
    val apellido: String? = null,
    val email: String? = null,
    val contrasena: String? = null,
    val fecha_registro: String? = null,  // Formato "yyyy-MM-dd"
    val id_suscripciones: Int? = null
)

@Serializable
data class HistorialActividad(
    val id_usuarios: Int,
    val id_ruta: Int,
    val fecha: String
)

@Serializable
data class SuscripcionAnuncio(
    val id_SA: Int,
    val id_suscripciones: Int,
    val id_anuncios: Int,
    val num_veces_mostrado: Int
)

// 🔒 Seguridad y JWT
@Serializable
data class UserCreate(
    val nombre: String,
    val apellido: String,
    val email: String,
    val contrasena: String
)

@Serializable
data class UpdatePasswordRequest(
    val contrasena_actual: String,
    val nueva_contrasena: String
)

@Serializable
data class UserResponse(
    val id_usuarios: Int,
    val nombre: String? = null,
    val apellido: String? = null,
    val email: String? = null,
    val fecha_registro: String? = null,
    val id_suscripciones: Int? = null
)

@Serializable
data class UpdateUserRequest(
    val nombre: String? = null,
    val apellido: String? = null,
    val email: String? = null
)

@Serializable
data class UpdateSuscriptionUserModel(
    val id_suscripcion: Int,
    val duracion: Int  // Duración en meses (1 o 12)
)

// Requiere Login
@Serializable
data class DeleteUser(
    val password: String
)

@Serializable
data class ResetPasswordRequest(
    val nueva_contrasena: String
)