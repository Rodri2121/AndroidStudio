package com.example.logintry.ui.theme.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintry.ui.theme.model.RegisterRequest

import kotlinx.coroutines.launch
import com.example.logintry.ui.theme.network.RetrofitClient
import androidx.compose.runtime.State
import com.example.logintry.ui.theme.model.RegisterState


class RegisterViewModel : ViewModel() {
    // Cambia MutableStateFlow a MutableState
    private val _registerState = mutableStateOf<RegisterState>(RegisterState.Idle)
    val registerState: State<RegisterState> get() = _registerState

    fun register(
        username: String,
        password: String,
        firstname: String,
        lastname: String,
        pais: String
    ) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.register(
                    RegisterRequest(username, password, firstname, lastname, pais)
                )
                _registerState.value = if (response.isSuccessful) {
                    RegisterState.Success(response.body()?.message ?: "Registro exitoso")
                } else {
                    RegisterState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Error de red: ${e.message}")
            }
        }
    }
}

// Estados para el registro
