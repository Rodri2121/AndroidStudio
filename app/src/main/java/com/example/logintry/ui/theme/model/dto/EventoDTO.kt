package com.example.logintry.ui.theme.model.dto

data class EventoDTO (
    val id: Int,
    val nombreEvento: String? = "",
    val fechaInicio: String? = "",
    val fechaFin: String? = "",
    val profesorId: Int,
    val nombreProfesor: String = "", // Nuevo campo calculado
    val estudiantes: List<EstudianteConEventosDTO>? = emptyList(),
    val estudiantesIds: List<Int>? = emptyList()

)
