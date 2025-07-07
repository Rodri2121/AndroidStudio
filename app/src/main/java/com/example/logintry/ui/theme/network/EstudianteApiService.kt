package com.example.logintry.ui.theme.network

import com.example.logintry.ui.theme.model.dto.EstudianteConEventosDTO
import com.example.logintry.ui.theme.model.dto.EstudianteDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EstudianteApiService {
    @GET("api/estudiantes/allWithEvents")
    suspend fun obtenerEstudiantesConEventos(): Response<List<EstudianteConEventosDTO>>

    @GET("api/estudiantes/all")
    suspend fun obtenerEstudiantes(): Response<List<EstudianteDTO>>

    @POST("api/estudiantes/save")
    suspend fun crearEstudiante(@Body estudiante: EstudianteDTO): Response<EstudianteDTO>





}