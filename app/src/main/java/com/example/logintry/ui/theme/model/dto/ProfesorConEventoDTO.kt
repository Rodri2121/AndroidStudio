package com.example.logintry.ui.theme.model.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProfesorConEventoDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombre") val nombre: String,
    val eventos: List<EventoDTO>?= emptyList()

)