package com.example.logintry.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.example.logintry.ui.theme.ViewModel.EstudianteViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

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

import androidx.compose.ui.unit.dp


import com.example.logintry.ui.theme.util.Resource

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.logintry.R
import com.example.logintry.ui.theme.ViewModel.EstudianteViewModelFactory
import com.example.logintry.ui.theme.model.dto.EstudianteConEventosDTO


import com.example.logintry.ui.theme.model.dto.EventoDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudiantesListScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: EstudianteViewModel = viewModel(
        factory = EstudianteViewModelFactory(context)
    )

    val state by viewModel.estudiantesConEventosState
    var estudianteSeleccionado by remember { mutableStateOf<EstudianteConEventosDTO?>(null) }

    LaunchedEffect(Unit) {
        viewModel.obtenerEstudiantesConEventos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier =  Modifier.height(70.dp),
                title = {
                    Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center){
                        Text("Lista de Estudiantes",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(R.drawable.stacked_waves_haikei),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.01f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(padding)
            )
            when (val current = state) {
                is Resource.Loading -> FullScreenLoader()
                is Resource.Success -> StudentListContent(
                    estudiantes = current.data ?: emptyList(),
                    onItemClick = { estudianteSeleccionado = it },
                    modifier = Modifier.padding(padding)
                )
                is Resource.Error -> ErrorState(
                    message = current.message ?: "Error desconocido",
                    onRetry = { viewModel.obtenerEstudiantesConEventos() },
                    modifier = Modifier.padding(padding)
                )
                else -> {}
            }

            estudianteSeleccionado?.let { estudiante ->
                EventosDialog(
                    estudiante = estudiante,
                    onDismiss = { estudianteSeleccionado = null }
                )
            }
        }

    }
}

@Composable
private fun StudentListContent(
    estudiantes: List<EstudianteConEventosDTO>,
    onItemClick: (EstudianteConEventosDTO) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(estudiantes, key = { it.id ?: 0 }) { estudiante ->
            StudentCard(
                estudiante = estudiante,
                onClick = { onItemClick(estudiante) }
            )
        }
    }
}

@Composable
private fun StudentCard(
    estudiante: EstudianteConEventosDTO,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = estudiante.nombre ?: "Sin nombre",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${estudiante.eventos?.size ?: 0} evento(s)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun EventosDialog(
    estudiante: EstudianteConEventosDTO,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismiss,
        title = { Text("Eventos de ${estudiante.nombre ?: "Estudiante"}") },
        text = {
            Column {
                when {
                    estudiante.eventos.isNullOrEmpty() -> {
                        Text("No hay eventos registrados")
                    }
                    else -> {
                        estudiante.eventos.forEachIndexed { index, evento ->
                            EventoItem(evento = evento)
                            if (index < estudiante.eventos.size - 1) {
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun EventoItem(evento: EventoDTO) {
    Column {
        Text(
            text = evento.nombreEvento ?: "Evento sin nombre",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = buildAnnotatedString {
                append(evento.fechaInicio?.toString() ?: "Fecha desconocida")
                append(" - ")
                append(evento.fechaFin?.toString() ?: "Fecha desconocida")
            },
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Profesor: ${evento.nombreProfesor?: "Profesor desconocido"}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun FullScreenLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}

