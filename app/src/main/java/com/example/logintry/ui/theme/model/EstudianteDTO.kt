package com.example.logintry.ui.theme.model

import com.google.gson.annotations.SerializedName

data class EstudianteDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombre") val nombre: String
)
