package com.example.logintry.ui.theme.ViewModel

import android.content.Context
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.viewModelScope



import com.example.logintry.ui.theme.network.RetrofitClient
import com.example.logintry.ui.theme.util.Resource
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.logintry.ui.theme.model.dto.EstudianteConEventosDTO
import com.example.logintry.ui.theme.model.dto.EstudianteDTO


class EstudianteViewModel (
    context: Context


) : ViewModel() {
    private val apiService = RetrofitClient.createEstudianteApiService(context)
    private val profesorApiService = RetrofitClient.createProfesorApiService(context)

    private val _state = mutableStateOf<Resource<EstudianteDTO>>(Resource.Idle)
    val state: State<Resource<EstudianteDTO>>
        get() = _state

    private val _estudiantesConEventosState = mutableStateOf<Resource<List<EstudianteConEventosDTO>>>(Resource.Idle)
    val estudiantesConEventosState: State<Resource<List<EstudianteConEventosDTO>>> get() = _estudiantesConEventosState

    // Cache para nombres de profesores
    private val _profesoresCache = mutableStateMapOf<Int, String>()

    fun obtenerEstudiantesConEventos() {
        viewModelScope.launch {
            _estudiantesConEventosState.value = Resource.Loading
            try {
                val response = apiService.obtenerEstudiantesConEventos()
                if(response.isSuccessful){
                    response.body()?.let {estudiantes ->
                        // Obtener nombres de profesores en paralelo
                        val estudiantesActualizados = estudiantes.map { estudiante ->
                            estudiante.copy(
                                eventos = estudiante.eventos?.map { evento ->
                                    evento.copy(
                                        nombreProfesor = obtenerNombreProfesor(evento.profesorId)
                                    )
                                }
                            )
                        }
                        _estudiantesConEventosState.value = Resource.Success(estudiantesActualizados)
                    } ?: run {
                        _estudiantesConEventosState.value = Resource.Error("Respuesta vacía")
                    }
                }else{
                    _estudiantesConEventosState.value = Resource.Error("Error: ${response.code()}")
                }
            }catch (e: Exception){
                _estudiantesConEventosState.value = Resource.Error("Error de red: ${e.message}")
            }
        }
    }

    private suspend fun obtenerNombreProfesor(profesorId: Int): String {
        return _profesoresCache.getOrPut(profesorId) {
            try {
                profesorApiService.obtenerProfesor(profesorId).body()?.nombre ?: "Profesor $profesorId"
            } catch (e: Exception) {
                "Profesor $profesorId"
            }
        }
    }
    fun guardarEstudiante(estudianteDTO: EstudianteDTO) {
        _state.value = Resource.Loading
        viewModelScope.launch {
            try {
                val response = apiService.crearEstudiante(estudianteDTO)
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
}


