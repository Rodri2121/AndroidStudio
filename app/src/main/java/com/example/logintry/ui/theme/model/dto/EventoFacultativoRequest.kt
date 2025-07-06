package com.example.logintry.ui.theme.model.dto

data class EventoFacultativoRequest(
    val nombreEvento: String,
    val fechaInicio: String,  // Formato "yyyy-MM-dd"
    val fechaFin: String,
    val profesorId: Int,
    val estudiantesIds: List<Int>,
)