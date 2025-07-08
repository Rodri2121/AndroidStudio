package com.example.logintry.ui.theme.network

import com.example.logintry.ui.theme.model.dto.EventoDTO
import com.example.logintry.ui.theme.model.dto.EventoFacultativoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EventoApiService {
    @GET("api/eventos/all")
    suspend fun obtenerEventos(): Response<List<EventoDTO>>
    @POST("api/eventos/profesor/{profesorId}/save")
    suspend fun crearEvento(
        @Path("profesorId") profesorId: Int,
        @Body evento: EventoFacultativoRequest
    ): Response<EventoDTO>
    @DELETE("api/eventos/delete/{id}")
    suspend fun eliminarEvento(@Path("id") id: Int): Response<Unit>
    @PUT("api/eventos/update/{id}")
    suspend fun actualizarEvento(
        @Path("id") id: Int,
        @Body evento: EventoFacultativoRequest
    ): Response<EventoDTO>


}