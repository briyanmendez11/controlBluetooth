package com.bradmr.control.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bradmr.control.ui.screens.*
import com.bradmr.control.viewmodel.MainViewModel

/**
 * Grafo de navegación centralizado de la aplicación.
 */
@Composable
fun NavGraph(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        
        // Pantalla 1: Bienvenida
        composable("welcome") {
            WelcomeScreen(
                onStartClick = { navController.navigate("bluetooth") }
            )
        }

        // Pantalla 2: Conexión Bluetooth
        composable("bluetooth") {
            BluetoothScreen(
                viewModel = viewModel,
                onContinue = { navController.navigate("leccion") }
            )
        }

        // Pantalla 3: Visualización de Lección y Pregunta
        composable("leccion") {
            LeccionScreen(
                viewModel = viewModel,
                onNavigateToControl = { navController.navigate("control") }
            )
        }

        // Pantalla 4: Control Remoto del Carrito
        composable("control") {
            ControlScreen(
                viewModel = viewModel,
                onFinish = {
                    if (viewModel.isLastLeccion.value) {
                        navController.navigate("completion")
                    } else {
                        viewModel.siguienteLeccion()
                        navController.navigate("leccion") {
                            popUpTo("leccion") { inclusive = true }
                        }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla 5: Finalización del Curso
        composable("completion") {
            CompletionScreen(
                onRestart = {
                    navController.navigate("welcome") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}
