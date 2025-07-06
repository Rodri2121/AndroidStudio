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
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.buildAnnotatedString
import com.example.logintry.ui.theme.ViewModel.ProfesorViewModel
import com.example.logintry.ui.theme.ViewModel.ProfesorViewModelFactory
import com.example.logintry.ui.theme.model.dto.EventoDTO
import com.example.logintry.ui.theme.model.dto.ProfesorConEventoDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfesoresListScreen(
    onBack: () -> Unit,
){
    val context = LocalContext.current

    val viewModel: ProfesorViewModel = viewModel(
        factory = ProfesorViewModelFactory(context)
    )

    var profesorSeleccionado by remember { mutableStateOf<ProfesorConEventoDTO?>(null) }

    val profesoresConEventosState by viewModel.profesoresConEventosState



    // Cargar profesores al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.obtenerProfesoresConEventos()
    }



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
        when (val current = profesoresConEventosState) {
            is Resource.Loading -> FullScreenLoader()
            is Resource.Success -> ProfesorListContent(
                profesores = current.data ?: emptyList(),
                onItemClick = { profesorSeleccionado = it },
                modifier = Modifier.padding(padding)
            )
            is Resource.Error -> ErrorState(
                message = current.message ?: "Error desconocido",
                onRetry = { viewModel.obtenerProfesoresConEventos() },
                modifier = Modifier.padding(padding)
            )
            else -> {}
        }
        profesorSeleccionado?.let { profesor ->
            ProfesorDialog(
                profesor = profesor,
                onDismiss = { profesorSeleccionado = null }
            )
        }
    }
}




//@Composable
//fun ProfesorItem(profesor: ProfesorDTO) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                text = profesor.nombre,
//                style = MaterialTheme.typography.headlineSmall
//            )
//            // Se eliminó la condición de especialidad
//        }
//    }
//}

@Composable
private fun ProfesorListContent(
    profesores: List<ProfesorConEventoDTO>,
    onItemClick: (ProfesorConEventoDTO) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(profesores, key = { it.id ?: 0 }) { profesor ->
            ProfesorCard(
                profesor = profesor,
                onClick = { onItemClick(profesor) }
            )
        }
    }
}



@Composable
private fun ProfesorCard(

    profesor: ProfesorConEventoDTO,
    onClick: () -> Unit

){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = profesor.nombre ?: "Sin nombre",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${profesor.eventos?.size ?: 0} evento(s)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }
    }
}
@Composable
private fun ProfesorEventoItem(evento: EventoDTO) {

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
//        Text(
//            text = "Profesor: ${evento.nombreProfesor?: "Profesor desconocido"}",
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
//        )
    }

}

@Composable
private fun ProfesorDialog(
    profesor: ProfesorConEventoDTO,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eventos de ${profesor.nombre ?: "Profesor"}") },
        text = {
            Column {
                when {
                    profesor.eventos.isNullOrEmpty() -> {
                        Text("No hay eventos registrados")
                    }

                    else -> {
                        profesor.eventos.forEachIndexed { index, evento ->
                            ProfesorEventoItem(evento = evento)
                            if (index < profesor.eventos.size - 1) {
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
