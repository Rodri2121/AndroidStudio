package com.example.logintry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logintry.ui.theme.screen.AddEstudianteScreen
import com.example.logintry.ui.theme.screen.AdminMenuScreen


import com.example.logintry.ui.theme.screen.LoginScreen
import com.example.logintry.ui.theme.screen.RegisterScreen
import com.example.logintry.ui.theme.screen.AddProfesorScreen
import com.example.logintry.ui.theme.screen.CreateEventoFacultativoScreen
import com.example.logintry.ui.theme.screen.EstudiantesListScreen
import com.example.logintry.ui.theme.screen.EventoFacultativoScreen
import com.example.logintry.ui.theme.screen.ProfesoresListScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("adminMenu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        // Nueva pantalla de administrador
        composable("adminMenu") {
            AdminMenuScreen(
                onNavigateToCreateEvent = {
                    navController.navigate("Crear Evento") },
                onNavigateToEventDetails = {
                    navController.navigate("eventos")
                },
                onNavigateToAddTeacher = {
                    navController.navigate("addProfesor")
                },
                onNavigateToAddStudent = {
                    navController.navigate("AddEstudiante")
                                         },
                onNavigateToGetStudent = {
                    navController.navigate("estudiantes")
                                         },
                onNavigateToGetTeacher = {
                    navController.navigate("profesores")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
        composable("addProfesor") {
            AddProfesorScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("addEstudiante") {
            AddEstudianteScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("profesores") {
            ProfesoresListScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla de Registro
        composable("register") {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack() // Vuelve atrás (a login)
                },
                onRegisterSuccess = {
                    // Opción 1: Ir directamente a home tras registro
//                    navController.navigate("home") {
//                        popUpTo("login") { inclusive = true }
//                    }

                    // Opción 2: O volver a login para que el usuario inicie sesión
                    navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("Crear Evento"){
            CreateEventoFacultativoScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable ("Estudiantes"){
            EstudiantesListScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("eventos"){
            EventoFacultativoScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla de Home
        composable("home") {
            HomeScreen(
                onLogout = {
                    // Navega a login y limpia el back stack
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}



@Composable
fun HomeScreen(
    onLogout: () -> Unit  // Elimina el valor por defecto = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Estas viendo la pantalla sin nada",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Ejecuta la acción de logout
                onLogout()

                // Opcional: Limpiar datos/estado aquí si es necesario
                // viewModel.clearSession()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}