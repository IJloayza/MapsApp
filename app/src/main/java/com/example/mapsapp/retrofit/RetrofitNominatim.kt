package com.example.mapsapp.retrofit

import com.example.mapsapp.retrofit.models.Lugar
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimService {
    //Unico query para el searchText en MapScreen
    @GET("search")
    suspend fun buscarLugares(
        @Query("q") name: String,
        @Query("limit") limit: Int = 5,
        @Query("format") format: String = "json"
    ): Response<List<Lugar>>
}

object RetrofitClient {
    val instance: NominatimService by lazy {
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NominatimService::class.java)
    }
}