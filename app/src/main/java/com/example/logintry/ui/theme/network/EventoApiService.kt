package com.example.logintry.ui.theme.network

import com.example.logintry.ui.theme.model.EventoDTO
import retrofit2.Response
import retrofit2.http.GET

interface EventoApiService {
    @GET("api/eventos/all")
    suspend fun obtenerEventos(): Response<List<EventoDTO>>
}