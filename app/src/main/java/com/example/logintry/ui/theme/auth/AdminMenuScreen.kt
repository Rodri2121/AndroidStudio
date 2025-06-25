package com.example.logintry.ui.theme.auth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.unit.dp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.logintry.ui.theme.model.LoginState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMenuScreen(
    onNavigateToCreateEvent: () -> Unit = {},
    onNavigateToEventDetails: () -> Unit = {},
    onNavigateToAddTeacher: () -> Unit = {},
    onNavigateToAddStudent: () -> Unit = {},
    onNavigateToGetStudent: () -> Unit = {},
    onNavigateToGetTeacher: () -> Unit = {},
    onLogout: () -> Unit = {}

) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menú de Administrador") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Crear Evento
            MenuButton(
                icon = Icons.Default.Add,
                text = "Crear Evento",
                onClick = onNavigateToCreateEvent
            )

            // 2. Detalles del Evento
            MenuButton(
                icon = Icons.AutoMirrored.Filled.List,
                text = "Detalles del Evento",
                onClick = onNavigateToEventDetails
            )

            // 3. Agregar Maestro
            MenuButton(
                icon = Icons.Default.Person,
                text = "Agregar Maestro",
                onClick = onNavigateToAddTeacher
            )

            // 4. Agregar Estudiante
            MenuButton(
                icon = Icons.Default.Person,
                text = "Agregar Estudiante",
                onClick = onNavigateToAddStudent
            )

            // 5. Obtener Estudiante
            MenuButton(
                icon = Icons.Default.Search,
                text = "Obtener Estudiante",
                onClick = onNavigateToGetStudent
            )

            // 6. Obtener Maestro
            MenuButton(
                icon = Icons.Default.Search,
                text = "Obtener Maestro",
                onClick = onNavigateToGetTeacher
            )
        }
    }
}

@Composable
private fun MenuButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
            Text(text)
        }
    }
}