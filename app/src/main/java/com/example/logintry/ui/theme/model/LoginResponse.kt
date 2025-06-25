package com.example.logintry.ui.theme.model

data class LoginResponse(
    val token: String,
    val message: String,
    val success: Boolean
)
