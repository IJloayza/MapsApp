package com.example.mapsapp.retrofit.models

data class SuscriptionType(
    val tipe: String,
)

data class UpdateSuscription(
    val tipe:String,
    val price: Float
)