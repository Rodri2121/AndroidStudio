package com.example.logintry.ui.theme.ViewModel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintry.ui.theme.model.dto.EventoDTO
import com.example.logintry.ui.theme.network.RetrofitClient
import com.example.logintry.ui.theme.util.Resource
import kotlinx.coroutines.launch

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import com.example.logintry.ui.theme.model.dto.EstudianteDTO
import com.example.logintry.ui.theme.model.dto.EventoFacultativoRequest
import com.example.logintry.ui.theme.model.dto.ProfesorDTO
import java.net.ConnectException
import java.net.SocketTimeoutException

class EventoFacultativoViewModel(
    context: Context
) : ViewModel() {
    private val apiService = RetrofitClient.createEventoApiService(context)

    private val profesorApiService = RetrofitClient.createProfesorApiService(context)
    private val _eventosState = mutableStateOf<Resource<List<EventoDTO>>>(Resource.Idle)
    private val estudianteApiService = RetrofitClient.createEstudianteApiService(context)
    val eventosState: State<Resource<List<EventoDTO>>> get() = _eventosState


    private val _profesoresCache = mutableStateMapOf<Int, String>()

    fun crearEvento(
        nombreEvento: String,
        fechaInicio: String,
        fechaFin: String,
        profesorId: Int?,
        estudiantesIds: List<Int>
    ) {
        viewModelScope.launch {
            _eventosState.value = Resource.Loading

            // Validación mejorada
            when {
                profesorId == null || profesorId <= 0 -> {
                    _eventosState.value = Resource.Error("Seleccione un profesor válido")
                    return@launch
                }
                nombreEvento.isBlank() -> {
                    _eventosState.value = Resource.Error("Ingrese un nombre para el evento")
                    return@launch
                }
                fechaInicio.isBlank() || fechaFin.isBlank() -> {
                    _eventosState.value = Resource.Error("Seleccione ambas fechas")
                    return@launch
                }
            }

            try {

                val eventoRequest = EventoFacultativoRequest(
                    nombreEvento = nombreEvento,
                    fechaInicio = fechaInicio,
                    fechaFin = fechaFin,
                    profesorId = profesorId,
                    estudiantesIds = estudiantesIds

                )

                val response = apiService.crearEvento(
                    profesorId = profesorId,  // Va en el path
                    evento = eventoRequest    // Body sin profesorId
                )

                _eventosState.value = if (response.isSuccessful) {
                    response.body()?.let { evento ->
                        // Actualizar cache de nombres si es necesario
                        evento.profesorId?.let { id ->
                            _profesoresCache[id] = evento.nombreProfesor ?: "Profesor $id"
                        }
                        Resource.Success(listOf(evento))
                    } ?: Resource.Error("El servidor no devolvió datos")
                } else {
                    val errorMsg = try {
                        response.errorBody()?.string() ?: "Error ${response.code()}"
                    } catch (e: Exception) {
                        "Error ${response.code()}"
                    }
                    Resource.Error(errorMsg)
                }
            } catch (e: Exception) {
                _eventosState.value = when (e) {
                    is SocketTimeoutException -> Resource.Error("Tiempo de espera agotado")
                    is ConnectException -> Resource.Error("Sin conexión al servidor")
                    else -> Resource.Error("Error: ${e.message ?: "Desconocido"}")
                }
            }
        }
    }




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
    private val _profesores = mutableStateOf<List<ProfesorDTO>>(emptyList())
    val profesores: State<List<ProfesorDTO>> get() = _profesores

    fun obtenerProfesores(){
        viewModelScope.launch {
            try {
                val response = profesorApiService.obtenerProfesores()
                if (response.isSuccessful) {
                    _profesores.value = response.body() ?: emptyList()
                } else {
                    // puedes loguear el error si deseas
                }
            } catch (e: Exception) {
                // puedes mostrar un error si deseas
            }
        }
    }

    private val _estudiantes = mutableStateOf<List<EstudianteDTO>>(emptyList())
    val estudiantes: State<List<EstudianteDTO>> get() = _estudiantes

    fun obtenerEstudiantes() {
        viewModelScope.launch {
            try {
                val response = estudianteApiService.obtenerEstudiantes() // <-- Ajusta a tu endpoint real
                if (response.isSuccessful) {
                    _estudiantes.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Manejo de errores si es necesario
            }
        }
    }



}