package com.example.logintry.ui.theme.auth

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.logintry.ui.theme.model.ProfesorDTO
import com.example.logintry.ui.theme.network.ProfesorApiService
import com.example.logintry.ui.theme.network.RetrofitClient
import com.example.logintry.ui.theme.util.Resource
import kotlinx.coroutines.launch

class ProfesorViewModel(
    context: Context
) : ViewModel() {

    private val apiService = RetrofitClient.createProfesorApiService(context)

    private val _state = mutableStateOf<Resource<ProfesorDTO>>(Resource.Idle)
    val state: androidx.compose.runtime.State<Resource<ProfesorDTO>>
        get() = _state

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
}