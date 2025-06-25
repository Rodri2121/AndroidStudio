package com.example.logintry.ui.theme.auth

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintry.ui.theme.model.LoginRequest
import com.example.logintry.ui.theme.model.LoginState
import com.example.logintry.ui.theme.network.RetrofitClient
import com.example.logintry.ui.theme.network.TokenManager
import kotlinx.coroutines.launch

class LoginViewModel(private val context: Context) : ViewModel() {

    private var _loginState by mutableStateOf<LoginState>(LoginState.Idle)
    val loginState: LoginState get() = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState = LoginState.Loading
            try {
                val response = RetrofitClient.apiService.login(
                    LoginRequest(username, password)
                )
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (!token.isNullOrBlank()) {
                        TokenManager.saveToken(context, token)
                        _loginState = LoginState.Success
                    } else {
                        _loginState = LoginState.Error("Token vacío")
                    }
                } else {
                    _loginState = LoginState.Error("Credenciales incorrectas")
                }
            } catch (e: Exception) {
                _loginState = LoginState.Error("Error de conexión: ${e.message}")
            }
        }
    }
}

