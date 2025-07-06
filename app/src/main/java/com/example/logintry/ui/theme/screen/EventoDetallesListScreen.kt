package com.example.logintry.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.logintry.ui.theme.ViewModel.EventoFacultativoViewModel
import com.example.logintry.ui.theme.ViewModel.EventoFacultativoViewModelFactory
import com.example.logintry.ui.theme.model.dto.EventoDTO


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoFacultativoScreen(
    onBack: () -> Unit,
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
                title = { Text("Lista de Eventos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
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




            else -> {}
        }

        // Diálogo fuera del when pero dentro del Scaffold
        eventoSeleccionado?.let { evento ->
            EventoDialog (
                evento = evento,
                onDismiss = { eventoSeleccionado = null }
            )
        }
    } // Cierre del Scaffold
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
        elevation = CardDefaults.cardElevation(4.dp)
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
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalles del Evento") },
        text = {
            Column {
                when{
                    evento.nombreEvento.isNullOrEmpty() -> {
                        Text("No hay eventos registrados")
                    }
                    else -> {
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
                }

            }
        }, // Cierre del text
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    ) // Cierre del AlertDialog
}

//@Composable
//private fun EventoDialog(
//    evento: EventoDTO,
//    onDismiss: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Detalles del Evento") },
//        text = {
//            Column {
//                when{
//                    evento.nombreEvento.isNullOrEmpty() -> {
//                        Text("No hay eventos registrados")
//                    }
//                    else -> {
//                        evento.nombreEvento.forEachIndexed { index, evento ->
//                            EventosItem(evento = evento)
//                            if (index < evento.size - 1) {
//                                Divider(modifier = Modifier.padding(vertical = 8.dp))
//                            }
//                        }
//                    }
//
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = onDismiss) {
//                Text("Cerrar")
//            }
//        }
//    )
//}


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
