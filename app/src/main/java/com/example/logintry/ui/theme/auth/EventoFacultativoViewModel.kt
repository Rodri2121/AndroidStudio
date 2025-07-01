package com.example.logintry.ui.theme.auth

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintry.ui.theme.model.EventoDTO
import com.example.logintry.ui.theme.network.RetrofitClient
import com.example.logintry.ui.theme.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf

class EventoFacultativoViewModel(
    context: Context
) : ViewModel() {
    private val apiService = RetrofitClient.createEventoApiService(context)
    private val profesorApiService = RetrofitClient.createProfesorApiService(context)
    private val _eventosState = mutableStateOf<Resource<List<EventoDTO>>>(Resource.Idle)
    val eventosState: State<Resource<List<EventoDTO>>> get() = _eventosState


    private val _profesoresCache = mutableStateMapOf<Int, String>()

    fun obtenerEventos() {
        viewModelScope.launch {
            _eventosState.value = Resource.Loading
            _eventosState.value = try {
                val response = apiService.obtenerEventos()
                if (response.isSuccessful) {
                    response.body()?.let { eventos ->
                        // Procesamos los eventos para obtener nombres de profesores
                        val eventosConProfesores = eventos.map { evento ->
                            evento.copy(
                                nombreProfesor = (if (evento.profesorId != null) {
                                    obtenerNombreProfesor(evento.profesorId)
                                } else {
                                    null
                                }).toString()
                            )
                        }
                        Resource.Success(eventosConProfesores)
                    } ?: Resource.Error("Respuesta vacía del servidor.")
                } else {
                    Resource.Error("Error del servidor: Código ${response.code()}.")
                }
            } catch (e: Exception) {
                Resource.Error("Error de conexión: ${e.localizedMessage ?: "Verifica tu conexión a internet."}")
            }
        }
    }
    private suspend fun obtenerNombreProfesor(profesorId: Int): String {
        return _profesoresCache.getOrPut(profesorId) {
            try {
                profesorApiService.obtenerProfesor(profesorId).body()?.nombre
                    ?: "Profesor $profesorId"
            } catch (e: Exception) {
                "Profesor $profesorId"

            }
        }
    }

}