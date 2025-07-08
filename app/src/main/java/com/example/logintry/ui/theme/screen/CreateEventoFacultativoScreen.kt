package com.example.logintry.ui.theme.screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField


import androidx.compose.material3.Text



import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf


import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import com.example.logintry.R
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

    LaunchedEffect(Unit) {
        viewModel.obtenerProfesores()
        viewModel.obtenerEstudiantes()
    }

    val profesores = viewModel.profesores.value
    val estudiantes = viewModel.estudiantes.value
    val state by viewModel.eventosState

    var nombreEvento by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var profesorExpandido by remember { mutableStateOf(false) }
    var profesorSeleccionado by remember { mutableStateOf("") }
    var profesorIdSeleccionado: Int? by remember { mutableStateOf(-1) }
    val estudiantesSeleccionados = remember { mutableStateListOf<Int>() }

    Box(modifier = Modifier.fillMaxSize()) {
        //Fondo decorativo estilo
        Image(
            painter = painterResource(R.drawable.stacked_peaks_haikei),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.12f)
        )

        //Botón volver
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
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        val tarjeta = Color(0xFFE0F2F1)

        //Tarjeta de contenido
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()){
                Image(
                    painter = painterResource(R.drawable.stacked_peaks_haikei),
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
                                    Color.Black.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Crear evento",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xCC182425),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    //Nombre
                    OutlinedTextField(
                        value = nombreEvento,
                        onValueChange = { nombreEvento = it },
                        label = { Text("Nombre del evento*") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Build, contentDescription = null) },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00796B),
                            unfocusedIndicatorColor = Color(0xFFB2DFDB),
                            cursorColor = Color(0xFF00796B)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    //Fecha inicio
                    DatePickerField("Fecha de inicio", fechaInicio) { fechaInicio = it }

                    Spacer(modifier = Modifier.height(12.dp))

                    //Fecha fin
                    DatePickerField("Fecha de fin", fechaFin) { fechaFin = it }

                    Spacer(modifier = Modifier.height(12.dp))

                    //Selector profesor
                    Box {
                        OutlinedTextField(
                            value = profesorSeleccionado,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { profesorExpandido = true },
                            label = { Text("Profesor*") },
                            readOnly = true,
                            shape = RoundedCornerShape(16.dp),
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color(0xFF00796B),
                                unfocusedIndicatorColor = Color(0xFFB2DFDB),
                                cursorColor = Color(0xFF00796B)
                            )
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

                    // Estudiantes
                    Text(
                        text = "Seleccionar estudiantes",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xCC182425),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    estudiantes.forEach { estudiante ->
                        val isChecked = estudiante.id in estudiantesSeleccionados
                        val buttonColor = Color(0xFFE0F2F1)
                        val contentColor = Color(0xFFFFFFFF)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(contentColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isChecked) estudiantesSeleccionados.remove(estudiante.id)
                                        else estudiante.id?.let { estudiantesSeleccionados.add(it) }
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = {
                                        if (it) estudiante.id?.let { id -> estudiantesSeleccionados.add(id) }
                                        else estudiante.id?.let { estudiantesSeleccionados.remove(it) }
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(estudiante.nombre)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón
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
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        if (state is Resource.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Crear evento",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }


        if (state is Resource.Success<*>) {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Evento creado correctamente", Toast.LENGTH_SHORT).show()
                delay(1000)
                onBack()
            }
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
        placeholder = { Text("DD/MM/AAAA") },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color(0xFF00796B),
            unfocusedIndicatorColor = Color(0xFFB2DFDB),
            cursorColor = Color(0xFF00796B)
        )
    )
}
