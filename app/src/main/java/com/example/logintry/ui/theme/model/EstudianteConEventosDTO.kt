package com.example.logintry.ui.theme.model
import com.google.gson.annotations.SerializedName

data class EstudianteConEventosDTO(
   val id: Int,
    val nombre: String,
    val eventos: List<EventoDTO>?= emptyList()
)
