package com.bradmr.control.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.bradmr.control.bluetooth.BluetoothController
import com.bradmr.control.ui.components.BigButton
import com.bradmr.control.ui.components.InfoCard
import com.bradmr.control.viewmodel.MainViewModel

/**
 * Pantalla de control remoto para el carrito Arduino.
 */
@Composable
fun ControlScreen(
    viewModel: MainViewModel,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val leccion = viewModel.currentLeccion
    val connectionState by viewModel.connectionState.collectAsState()
    val isConnected = connectionState is BluetoothController.ConnectionState.Connected
    val practicaFinalizada = viewModel.practicaFinalizada.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Cabecera: Título e instrucción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Práctica: ${leccion.titulo}", style = MaterialTheme.typography.titleLarge)
                Text(text = leccion.instruccionPractica, style = MaterialTheme.typography.bodyMedium)
            }
            
            // Estado de conexión
            Surface(
                color = if (isConnected) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = if (isConnected) "Conectado" else "Desconectado",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Controles del Carrito (Joystick visual)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ControlButton(Icons.Default.KeyboardArrowUp, "F", "Avanzar", viewModel, isConnected)
                    Row {
                        ControlButton(Icons.Default.KeyboardArrowLeft, "L", "Izquierda", viewModel, isConnected)
                        Spacer(modifier = Modifier.width(16.dp))
                        ControlButton(Icons.Default.Stop, "S", "Detener", viewModel, isConnected, Color.Red)
                        Spacer(modifier = Modifier.width(16.dp))
                        ControlButton(Icons.Default.KeyboardArrowRight, "R", "Derecha", viewModel, isConnected)
                    }
                    ControlButton(Icons.Default.KeyboardArrowDown, "B", "Atrás", viewModel, isConnected)
                }
            }

            // Acciones laterales
            Column(
                modifier = Modifier.weight(0.4f).padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (!practicaFinalizada) {
                    BigButton(
                        text = "Finalizar Práctica",
                        onClick = { viewModel.finalizarPractica() },
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                } else {
                    InfoCard(title = "¡Excelente!") {
                        Text("Práctica finalizada. El evaluador puede registrar el resultado.")
                        Spacer(modifier = Modifier.height(8.dp))
                        BigButton(
                            text = "Siguiente Lección",
                            onClick = onFinish,
                            containerColor = Color(0xFF4CAF50)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a la lección")
                }
            }
        }
    }
}

@Composable
fun ControlButton(
    icon: ImageVector,
    command: String,
    contentDescription: String,
    viewModel: MainViewModel,
    enabled: Boolean,
    color: Color = MaterialTheme.colorScheme.primary
) {
    FilledIconButton(
        onClick = { viewModel.sendCommand(command) },
        enabled = enabled,
        modifier = Modifier.size(80.dp).padding(4.dp),
        colors = IconButtonDefaults.filledIconButtonColors(containerColor = color)
    ) {
        Icon(icon, contentDescription = contentDescription, modifier = Modifier.size(48.dp))
    }
}
