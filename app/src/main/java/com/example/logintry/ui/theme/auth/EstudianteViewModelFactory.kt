package com.example.logintry.ui.theme.auth
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EstudianteViewModelFactory(private val context: Context) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EstudianteViewModel(context) as T
    }
}