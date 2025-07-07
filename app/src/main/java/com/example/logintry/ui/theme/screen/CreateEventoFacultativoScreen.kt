package com.example.logintry.ui.theme.screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf


import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.compose.ui.Alignment
import androidx.compose.ui.zIndex
import com.example.logintry.ui.theme.util.Resource
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventoFacultativoScreen(
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: EventoFacultativoViewModel = viewModel(
        factory = EventoFacultativoViewModelFactory(context)
    )

    // Llama a obtenerProfesores solo una vez
    LaunchedEffect(Unit) {
        viewModel.obtenerProfesores()
        viewModel.obtenerEstudiantes()
    }



    val profesores = viewModel.profesores.value

    var nombreEvento by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }

    var profesorExpandido by remember { mutableStateOf(false) }
    var profesorSeleccionado by remember { mutableStateOf("") }
    var profesorIdSeleccionado: Int? by remember { mutableStateOf(-1) }

    val estudiantes = viewModel.estudiantes.value
    val estudiantesSeleccionados = remember { mutableStateListOf<Int>() }

    val state by viewModel.eventosState



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    shape = CircleShape
                )
                .zIndex(1f)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.TopCenter)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Registro de evento",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Crear evento",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = nombreEvento,
                    onValueChange = { nombreEvento = it },
                    label = { Text("Nombre del evento*") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Build, null) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                DatePickerField(
                    label = "Fecha de inicio",
                    selectedDate = fechaInicio,
                    onDateSelected = { fechaInicio = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DatePickerField(
                    label = "Fecha de fin",
                    selectedDate = fechaFin,
                    onDateSelected = { fechaFin = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box {
                    OutlinedTextField(
                        value = profesorSeleccionado,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { profesorExpandido = true },
                        label = { Text("Profesor*") },
                        readOnly = true,
                        leadingIcon = { Icon(Icons.Default.Person, null) }
                    )

                    DropdownMenu(
                        expanded = profesorExpandido,
                        onDismissRequest = { profesorExpandido = false }
                    ) {
                        profesores.forEach { profesor ->
                            DropdownMenuItem(
                                text = { Text(profesor.nombre) },
                                onClick = {
                                    profesorSeleccionado = profesor.nombre
                                    profesorIdSeleccionado = profesor.id
                                    profesorExpandido = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text("Seleccionar estudiantes", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                estudiantes.forEach { estudiante ->
                    val isChecked = estudiante.id in estudiantesSeleccionados
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isChecked) estudiantesSeleccionados.remove(estudiante.id)
                                else estudiante.id?.let { estudiantesSeleccionados.add(it) }
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                if (it) estudiante.id?.let { element -> estudiantesSeleccionados.add(element) }
                                else estudiantesSeleccionados.remove(estudiante.id)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(estudiante.nombre)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.crearEvento(
                            nombreEvento,
                            fechaInicio,
                            fechaFin,
                            profesorIdSeleccionado,
                            estudiantesSeleccionados.toList()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    if (viewModel.eventosState.value is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Crear evento",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                    }
                }
            }
        }
    }
    if(state is Resource.Success<*>){
        LaunchedEffect(Unit){
            Toast.makeText(context, "Evento creado correctamente", Toast.LENGTH_SHORT).show()
            delay(1000)
            onBack()
        }
    }
}



@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val dateStr = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelected(dateStr)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }


    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        label = { Text(label) },
        readOnly = true,
        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
        placeholder = { Text("DD/MM/AAAA") }
    )
}
