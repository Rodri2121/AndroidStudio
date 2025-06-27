package com.example.logintry.ui.theme.auth

import android.content.Context
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.viewModelScope



import com.example.logintry.ui.theme.network.RetrofitClient
import com.example.logintry.ui.theme.util.Resource
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.logintry.ui.theme.model.EstudianteDTO
import com.example.logintry.ui.theme.network.EstudianteApiService


import kotlinx.coroutines.launch


class EstudianteViewModel (
    context: Context


) : ViewModel() {
    private val apiService = RetrofitClient.createEstudianteApiService(context)

    private val _estudianteState = mutableStateOf<Resource<List<EstudianteDTO>>>(Resource.Idle)
    val estudianteState: State<Resource<List<EstudianteDTO>>> get() = _estudianteState

    fun obtenerEstudiantesConEventos() {
        viewModelScope.launch {
            _estudianteState.value = Resource.Loading
            try {
                val response = apiService.obtenerEstudiantesConEventos()
            }catch (e: Exception){
                _estudianteState.value = Resource.Error("Error de red: ${e.message}")
            }
        }
    }
}


