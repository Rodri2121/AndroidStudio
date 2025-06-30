package com.example.logintry.ui.theme.screen


import android.widget.Toast
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintry.ui.theme.auth.ProfesorViewModel
import com.example.logintry.ui.theme.model.ProfesorDTO

import com.example.logintry.ui.theme.util.Resource
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProfesorScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: ProfesorViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfesorViewModel(context) as T
            }
        }
    )

    var nombre by remember { mutableStateOf("") }
    val state by viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Profesor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del profesor") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.guardarProfesor(ProfesorDTO(nombre = nombre))
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nombre.isNotBlank() && state !is Resource.Loading
            ) {
                if (state is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar")
                }
            }

            if (state is Resource.Error) {
                Text(
                    text = (state as Resource.Error).message ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    if (state is Resource.Success<*>) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Profesor agregado correctamente", Toast.LENGTH_SHORT).show()
            delay(1000)
            onBack()
        }
    }
}
