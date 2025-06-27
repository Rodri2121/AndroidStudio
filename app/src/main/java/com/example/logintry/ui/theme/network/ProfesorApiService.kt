package com.example.logintry.ui.theme.network

import com.example.logintry.ui.theme.model.ProfesorDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfesorApiService {
    @POST("api/profesores/save")
    suspend fun crearProfesor(@Body profesorDTO: ProfesorDTO): Response<ProfesorDTO>
    @GET("api/profesores/all")
    suspend fun obtenerProfesores(): Response<List<ProfesorDTO>>
}