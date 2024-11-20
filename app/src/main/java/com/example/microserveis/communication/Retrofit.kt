package com.example.microserveis.communication
import com.example.microserveis.data.ScriptStatus
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("/scripts")
    fun getScripts(): Call<List<String>>

    @POST("/scripts/{scriptName}/{action}")
    fun controlScript(
        @Path("scriptName") scriptName: String,
        @Path("action") action: String): Call<Map<String, String>>

    @GET("/scripts/{scriptName}/status")
    fun getScriptStatus(@Path("scriptName") scriptName: String): Call<ScriptStatus>
}