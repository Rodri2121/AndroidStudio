package com.example.logintry.ui.theme.network

import com.example.logintry.ui.theme.model.EstudianteDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface EstudianteApiService {
    @GET("api/estudiantes/allWithEvents")
    suspend fun obtenerEstudiantesConEventos(): Response<List<EstudianteDTO>>

}