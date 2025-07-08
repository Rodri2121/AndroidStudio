package com.example.logintry.ui.theme.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class EstudianteConEventosDTO(
   val id: Int,
    val nombre: String,
    val eventos: List<EventoDTO>?= emptyList()
)
