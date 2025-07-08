package com.example.logintry.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintry.ui.theme.ViewModel.EventoFacultativoViewModel
import com.example.logintry.ui.theme.ViewModel.EventoFacultativoViewModelFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope


import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.compose.ui.Alignment
import com.example.logintry.ui.theme.model.dto.EventoDTO
import com.example.logintry.ui.theme.model.dto.EventoFacultativoRequest
import com.example.logintry.ui.theme.util.Resource
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarEventoFacultativoScreen(
    eventoOriginal: EventoDTO,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: EventoFacultativoViewModel = viewModel(
        factory = EventoFacultativoViewModelFactory(context)
    )

    val scope = rememberCoroutineScope()

    var nombreEvento by remember { mutableStateOf(eventoOriginal.nombreEvento ?: "") }
    var fechaInicio by remember { mutableStateOf(eventoOriginal.fechaInicio ?: "") }
    var fechaFin by remember { mutableStateOf(eventoOriginal.fechaFin ?: "") }
    var profesorIdSeleccionado: Int? by remember { mutableStateOf(eventoOriginal.profesorId) }
    var profesorNombre by remember { mutableStateOf(eventoOriginal.nombreProfesor ?: "") }

    val estudiantes = viewModel.estudiantes.value
    val estudiantesSeleccionados = remember {
        mutableStateListOf<Int>().apply {
            eventoOriginal.estudiantesIds?.let { addAll(it) }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.obtenerProfesores()
        viewModel.obtenerEstudiantes()
    }

    val profesores = viewModel.profesores.value

    var profesorExpandido by remember { mutableStateOf(false) }

    val state by viewModel.eventosState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Editar Evento", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreEvento,
            onValueChange = { nombreEvento = it },
            label = { Text("Nombre del evento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(label = "Fecha de inicio", selectedDate = fechaInicio) {
            fechaInicio = it
        }

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(label = "Fecha de fin", selectedDate = fechaFin) {
            fechaFin = it
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = profesorNombre,
            onValueChange = {},
            label = { Text("Profesor") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { profesorExpandido = true },
            readOnly = true
        )

        DropdownMenu(
            expanded = profesorExpandido,
            onDismissRequest = { profesorExpandido = false }
        ) {
            profesores.forEach { profesor ->
                DropdownMenuItem(
                    text = { Text(profesor.nombre) },
                    onClick = {
                        profesorIdSeleccionado = profesor.id
                        profesorNombre = profesor.nombre
                        profesorExpandido = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Seleccionar estudiantes:", style = MaterialTheme.typography.titleMedium)

        estudiantes.forEach { estudiante ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = estudiantesSeleccionados.contains(estudiante.id),
                    onCheckedChange = { checked ->
                        if (checked) {
                            estudiante.id?.let { estudiantesSeleccionados.add(it) }
                        } else {
                            estudiantesSeleccionados.remove(estudiante.id)
                        }
                    }
                )
                Text(estudiante.nombre)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.actualizarEvento(
                    eventoOriginal.id,
                    EventoFacultativoRequest(
                        nombreEvento = nombreEvento,
                        fechaInicio = fechaInicio,
                        fechaFin = fechaFin,
                        profesorId = profesorIdSeleccionado,
                        estudiantesIds = estudiantesSeleccionados.toList()
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state is Resource.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Actualizar Evento")
            }
        }
    }

    if (state is Resource.Success<*>) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Evento actualizado", Toast.LENGTH_SHORT).show()
            delay(1000)
            onBack()
        }
    }
}


