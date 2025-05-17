package com.example.mapsapp.retrofit


import com.example.mapsapp.retrofit.models.DeleteUser
import com.example.mapsapp.retrofit.models.HistorialActividad
import com.example.mapsapp.retrofit.models.LoginRequest
import com.example.mapsapp.retrofit.models.LoginResponse
import com.example.mapsapp.retrofit.models.Mountain
import com.example.mapsapp.retrofit.models.ResetPasswordRequest
import com.example.mapsapp.retrofit.models.Route
import com.example.mapsapp.retrofit.models.SuscriptionType
import com.example.mapsapp.retrofit.models.UpdatePasswordRequest
import com.example.mapsapp.retrofit.models.UpdateRoute
import com.example.mapsapp.retrofit.models.UpdateSuscription
import com.example.mapsapp.retrofit.models.UpdateUserRequest
import com.example.mapsapp.retrofit.models.User
import com.example.mapsapp.retrofit.models.UserCreate
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface RetrofitPicotrakeService {

    //USUARIOS REQUEST
    @GET("/usuarios/{id_usuario}")
    suspend fun getUser(@Path("id_usuario")id: Int):Response<User?>

    @GET("/usuarios/{email:str}")
    suspend fun getUserByEmail(@Path("email:str") userEmail:String):Response<User?>

    @GET("/usuario/suscription/")
    suspend fun getUserSuscription(@Header("Authorization") token: String): Response<SuscriptionType>

    @POST("/usuarios")
    suspend fun createUser(@Body user: UserCreate):Response<Any>

    @POST("/login")
    suspend fun loginUser(@Body loginUser: LoginRequest):Response<LoginResponse>

    //Preguntar que es UpdateUserRequest
    //Requiere login
    @PUT("usuarios/update")
    suspend fun updateUser(@Header("Authorization") token: String, @Body request: UpdateUserRequest): Response<Any>

    //Require login
    @PUT("usuarios/update/password")
    suspend fun updatePassword(@Header("Authorization") token: String, @Body request: UpdatePasswordRequest): Response<Any>

    @PUT("/usuarios/reset/password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Any>

    //Requiere suscripcion
    @PUT("/usuarios/update/suscription")
    suspend fun updatePlan(@Body request: UpdateSuscription): Response<Any>

    @DELETE("/usuarios/")
    suspend fun deleteUser(@Header("Authorization") token: String, @Body request :DeleteUser): Response<Any>

    //RUTAS REQUEST
    @GET("/rutas")
    suspend fun getRoutes(): List<List<Route>>

    @GET("/rutas/{id_ruta}")
    suspend fun getRoute(@Path("id_ruta") id: Int): Response<Route>

    @GET("/rutas/{nombre_ruta}")
    suspend fun getRouteByName(@Path("nombre_ruta") name :String): Response<Route>

    @POST("/rutas")
    suspend fun createRoute(@Body route: Route): Response<Any>

    @PUT("/rutas/{nombre_ruta}")
    suspend fun updateNameRoute(@Body updateRoute: UpdateRoute): Response<Any>

    @DELETE("/rutas/{nombre_ruta}")
    suspend fun deleteRoute(@Path("/rutas/{nombre_ruta}") name : String): Response<Any>

    //Requiere Login
    @GET("/historial/usuario/mis-actividades")
    suspend fun getRoutesForUser(): Response<List<List<Route>>>

    @GET("/historial/usuario/{nombre_ruta}")
    suspend fun getRouteForUser(@Path("nombre_ruta") name: String): Response<List<List<Route>>>

    @PUT("/historial/")
    suspend fun createHistorial(@Body hist: HistorialActividad): Response<Any>

    @DELETE("/usuario/historial")
    suspend fun deleteHistorial(): Response<Any>

    @DELETE("/usuario/historial/{nombre_ruta}")
    suspend fun deleteRouteHistorial(@Path("nombre_ruta") name: String): Response<Any>

    //ANUNCIOS REQUEST
    //MONTAÑAS
    @GET("/mountains/")
    suspend fun getMountains(): Response<List<Mountain>>

    @GET("/mountains/{nombre_montanya}")
    suspend fun getMountainByName(@Path("nombre_montanya") name: String): Response<Mountain>
}

object RetrofitPicotrakeManager{
    private const val BASE_URL = "https://api.picotrakeclub.tech"

    //Desde aqui es posible colocar timeouts a las respuestas o asignar un Token si la app necesita uno
    private val client = getUnsafeOkHttpClient()

    //El retrofitService se inicia a partir de la instancia en ViewModels
    val instance: RetrofitPicotrakeService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RetrofitPicotrakeService::class.java)
    }
}

private fun getUnsafeOkHttpClient(): OkHttpClient {
    try {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
        )

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { hostname, session -> true }

        val okHttpClient = builder.build()
        return okHttpClient
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}