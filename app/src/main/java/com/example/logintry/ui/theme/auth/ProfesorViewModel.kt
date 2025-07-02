package com.example.logintry.ui.theme.auth

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.logintry.ui.theme.model.ProfesorDTO

import com.example.logintry.ui.theme.network.RetrofitClient
import com.example.logintry.ui.theme.util.Resource
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.logintry.ui.theme.model.ProfesorConEventoDTO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class ProfesorViewModel(
    context: Context
) : ViewModel() {

    private val apiService = RetrofitClient.createProfesorApiService(context)

    private val _state = mutableStateOf<Resource<ProfesorDTO>>(Resource.Idle)
    val state: State<Resource<ProfesorDTO>>
        get() = _state
    // Estado para obtener profesores
    private val _profesoresState = mutableStateOf<Resource<List<ProfesorDTO>>>(Resource.Idle)
    val profesoresState: State<Resource<List<ProfesorDTO>>> get() = _profesoresState

    private val _profesoresConEventosState = mutableStateOf<Resource<List<ProfesorConEventoDTO>>>(Resource.Idle)
    val profesoresConEventosState: State<Resource<List<ProfesorConEventoDTO>>>
        get() = _profesoresConEventosState


    fun guardarProfesor(profesorDTO: ProfesorDTO) {
        _state.value = Resource.Loading
        viewModelScope.launch {
            try {
                val response = apiService.crearProfesor(profesorDTO)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _state.value = Resource.Success(it)
                    } ?: run {
                        _state.value = Resource.Error("Respuesta vacía")
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error ${response.code()}"
                    _state.value = Resource.Error(errorBody)
                }
            } catch (e: Exception) {
                _state.value = Resource.Error("Error de red: ${e.message}", e)
            }
        }
    }
    fun obtenerProfesores() {
        viewModelScope.launch {
            _profesoresState.value = Resource.Loading
            try {
                val response = apiService.obtenerProfesores()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _profesoresState.value = Resource.Success(it)
                    }   ?: run {
                        _profesoresState.value = Resource.Error("Respuesta vacía")
                    }
                } else {
                    _profesoresState.value = Resource.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _profesoresState.value = Resource.Error("Error de red: ${e.message}")
            }
        }
    }
    fun obtenerProfesoresConEventos() {
        viewModelScope.launch {
            _profesoresConEventosState.value = Resource.Loading
            try {
                val profesoresResponse = apiService.obtenerProfesores()
                if (profesoresResponse.isSuccessful) {
                    val profesores = profesoresResponse.body() ?: emptyList()
                    val profesoresConEventos = profesores.map { profesor ->
                        val detalleResponse = apiService.obtenerEventosPorProfesor(profesor.id!!)
                        if (detalleResponse.isSuccessful) {
                            detalleResponse.body() ?: ProfesorConEventoDTO(profesor.id, profesor.nombre, emptyList())
                        } else {
                            ProfesorConEventoDTO(profesor.id, profesor.nombre, emptyList())
                        }
                    }
                    _profesoresConEventosState.value = Resource.Success(profesoresConEventos) as Resource<List<ProfesorConEventoDTO>>
                } else {
                    _profesoresConEventosState.value = Resource.Error("Error al obtener profesores")
                }
            } catch (e: Exception) {
                _profesoresConEventosState.value = Resource.Error("Error: ${e.message}")
            }
        }
    }

}