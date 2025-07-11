package com.example.logintry.ui.theme.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.logintry.R
import com.example.logintry.ui.theme.ViewModel.EventoFacultativoViewModel
import com.example.logintry.ui.theme.ViewModel.EventoFacultativoViewModelFactory
import com.example.logintry.ui.theme.model.dto.EventoDTO


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoFacultativoScreen(
    onBack: () -> Unit,
    onEditarEvento: (EventoDTO) -> Unit
) {
    val context = LocalContext.current
    val viewModel: EventoFacultativoViewModel = viewModel(
        factory = EventoFacultativoViewModelFactory(context)
    )

    var eventoSeleccionado by remember { mutableStateOf<EventoDTO?>(null) }

    LaunchedEffect(Unit) {
        viewModel.obtenerEventos()
    }

    val state by viewModel.eventosState

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
                        Text("Lista de Eventos",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    },


                navigationIcon = {
                    Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center){
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }

                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            // Fondo con imagen
            Image(
                painter = painterResource(R.drawable.stacked_waves_haikei),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradiente encima de la imagen
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
            )

            // Contenido
            when (val currentState = state) {
                Resource.Loading -> FullScreenLoader()
                is Resource.Success -> EventoListContent(
                    eventos = currentState.data ?: emptyList(),
                    onItemClick = { eventoSeleccionado = it },
                    modifier = Modifier.padding(padding)
                )
                is Resource.Error -> ErrorState(
                    message = currentState.message ?: "Error desconocido",
                    onRetry = { viewModel.obtenerEventos() },
                    modifier = Modifier.padding(padding)
                )
                else -> {
                }
            }

            eventoSeleccionado?.let { evento ->
                EventoDialog(
                    evento = evento,
                    onDismiss = { eventoSeleccionado = null },
                    onEditarEvento = {
                        eventoSeleccionado = null
                        onEditarEvento(it)
                    },
                    onEliminar = {
                        viewModel.eliminarEvento(it.id!!)
                        eventoSeleccionado = null
                    }
                )
            }
        }
    }
}






@Composable
private fun EventoListContent(
    eventos: List<EventoDTO>,
    onItemClick: (EventoDTO) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier.fillMaxSize()){
        items(eventos, key = { it.id ?: 0 }) { evento ->
            EventoCard(
                evento = evento,
                onClick = { onItemClick(evento) }
            )
        }
    }
}

@Composable
private fun EventoCard(
    evento: EventoDTO,
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
                text = evento.nombreEvento ?: "Sin nombre",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${evento.fechaInicio} - ${evento.fechaFin}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }
    } // Aquí cierra el Card
}

@Composable
private fun EventoDialog(
    evento: EventoDTO,
    onDismiss: () -> Unit,
    onEditarEvento: (EventoDTO) -> Unit,
    onEliminar: (EventoDTO) -> Unit = {}
) {
    var menuExpandido by remember { mutableStateOf(false) }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Detalles del Evento")
                Box {
                    IconButton(onClick = { menuExpandido = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Actualizar") },
                            onClick = {
                                menuExpandido = false
                                onEditarEvento(evento)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                menuExpandido = false
                                onEliminar(evento)
                            }
                        )
                    }
                }
            }
        },
        text = {
            Column {
                Text("Nombre del Evento: ${evento.nombreEvento ?: "No disponible"}")
                Text("Fecha de Inicio: ${evento.fechaInicio ?: "No disponible"}")
                Text("Fecha de Fin: ${evento.fechaFin ?: "No disponible"}")
                Text("Profesor Asignado: ${evento.nombreProfesor ?: "No asignado"}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Estudiantes Asignados:", style = MaterialTheme.typography.bodyMedium)
                if (evento.estudiantesIds.isNullOrEmpty()) {
                    Text("No hay estudiantes asignados", style = MaterialTheme.typography.bodySmall)
                } else {
                    Column {
                        evento.estudiantes?.forEach { estudiante ->
                            Text("• ${estudiante.nombre ?: "Estudiante sin nombre"}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
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
