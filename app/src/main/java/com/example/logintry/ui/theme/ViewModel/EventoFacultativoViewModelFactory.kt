package com.example.logintry.ui.theme.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModelProvider

class EventoFacultativoViewModelFactory(private val context: Context) : ViewModelProvider.Factory  {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return EventoFacultativoViewModel(context) as T
    }

}