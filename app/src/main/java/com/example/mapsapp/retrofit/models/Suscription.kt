package com.example.mapsapp.retrofit.models

import java.util.Optional

data class Suscription(
    val idSuscription: Int,
    val tipe: String,
    val price: Optional<Float>
)

data class SuscriptionAnuncio(
    val idSa: Int,
    val idSuscriptions: Int,
    val idAnunces: Int,
    val numShowed: Int
)

data class UpdateSuscription(
    val tipe:String,
    val price: Float
)