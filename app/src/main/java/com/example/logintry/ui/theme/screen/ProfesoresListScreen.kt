package com.example.logintry.ui.theme.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp

import com.example.logintry.ui.theme.model.ProfesorDTO

import com.example.logintry.ui.theme.util.Resource

import androidx.compose.foundation.lazy.items
import com.example.logintry.ui.theme.auth.ProfesorViewModel
import com.example.logintry.ui.theme.auth.ProfesorViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfesoresListScreen(
    onBack: () -> Unit,
){
    val context = LocalContext.current

    val viewModel: ProfesorViewModel = viewModel(
        factory = ProfesorViewModelFactory(context)
    )


    // Cargar profesores al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.obtenerProfesores()
    }

    val state by viewModel.profesoresState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Profesores") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when (val currentState = state) {
            Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val profesores = currentState.data
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    if (profesores.isEmpty()) {
                        item {
                            Text(
                                text = "No hay profesores registrados",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(items = profesores, key = { it.id ?: it.hashCode() }) { profesor ->
                            ProfesorItem(profesor = profesor)
                        }
                    }
                }
            }
            is Resource.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar profesores",
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(text = currentState.message ?: "Error desconocido")
                        Button(
                            onClick = { viewModel.obtenerProfesores() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun ProfesorItem(profesor: ProfesorDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = profesor.nombre,
                style = MaterialTheme.typography.headlineSmall
            )
            // Se eliminó la condición de especialidad
        }
    }
}