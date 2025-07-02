package com.example.logintry.ui.theme.model

import com.google.gson.annotations.SerializedName

data class ProfesorConEventoDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombre") val nombre: String,
    val eventos: List<EventoDTO>?= emptyList()

)