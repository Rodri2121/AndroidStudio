package com.example.logintry.ui.theme.network

import com.example.logintry.ui.theme.model.EventoDTO
import com.example.logintry.ui.theme.model.ProfesorConEventoDTO
import com.example.logintry.ui.theme.model.ProfesorDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfesorApiService {
    @POST("api/profesores/save")
    suspend fun crearProfesor(@Body profesorDTO: ProfesorDTO): Response<ProfesorDTO>
    @GET("api/profesores/all")
    suspend fun obtenerProfesores(): Response<List<ProfesorDTO>>
    @GET("api/profesores/{id}")
    suspend fun obtenerProfesor(@Path("id") id: Int): Response<ProfesorDTO>
    @GET("api/profesores/profe/{id}")
    suspend fun obtenerEventosPorProfesor(@Path("id") id: Int): Response<ProfesorConEventoDTO>


}