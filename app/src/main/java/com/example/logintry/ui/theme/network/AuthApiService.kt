package com.example.logintry.ui.theme.network

import com.example.logintry.ui.theme.model.LoginRequest
import com.example.logintry.ui.theme.model.LoginResponse
import com.example.logintry.ui.theme.model.RegisterRequest
import com.example.logintry.ui.theme.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login") // Ajusta este endpoint según tu API Spring Boot
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}