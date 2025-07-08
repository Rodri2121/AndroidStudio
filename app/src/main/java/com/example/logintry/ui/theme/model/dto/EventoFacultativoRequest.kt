package com.example.logintry.ui.theme.model.dto
import kotlinx.serialization.Serializable
@Serializable
data class EventoFacultativoRequest(
    val nombreEvento: String,
    val fechaInicio: String,  // Formato "yyyy-MM-dd"
    val fechaFin: String,
    val profesorId: Int?,
    val estudiantesIds: List<Int>,
)