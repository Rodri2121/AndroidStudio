package com.example.logintry.ui.theme.model.dto

import com.google.gson.annotations.SerializedName

data class ProfesorDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombre") val nombre: String
)
