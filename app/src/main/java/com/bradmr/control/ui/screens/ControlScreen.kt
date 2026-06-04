package com.bradmr.control.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bradmr.control.bluetooth.BluetoothController
import com.bradmr.control.ui.components.BigButton
import com.bradmr.control.ui.components.DirectionButton
import com.bradmr.control.ui.components.InfoCard
import com.bradmr.control.viewmodel.MainViewModel

/**
 * Pantalla de control remoto optimizada para manejo con dos manos en horizontal.
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
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Barra Superior: Navegación y Estado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(
                    text = leccion.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Surface(
                    color = if (isConnected) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = if (isConnected) "CARRITO CONECTADO" else "DESCONECTADO",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            BigButton(
                text = "Finalizar",
                onClick = { viewModel.finalizarPractica() },
                enabled = !practicaFinalizada,
                /*containerColor = MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier.height(40.dp)*/
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = leccion.instruccionPractica,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        // Zona de Controles: Distribución Ergonómica
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pulgar Izquierdo: Adelante y Atrás
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    DirectionButton(
                        icon = Icons.Default.KeyboardArrowUp,
                        onClick = { viewModel.sendCommand("F") },
                        enabled = isConnected && !practicaFinalizada
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    DirectionButton(
                        icon = Icons.Default.KeyboardArrowDown,
                        onClick = { viewModel.sendCommand("B") },
                        enabled = isConnected && !practicaFinalizada
                    )
                }

                // Centro: Botón Detener (Seguridad)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (practicaFinalizada) {
                        InfoCard(
                            title = "¡Muy bien!",
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.width(280.dp)
                        ) {
                            Text("Has terminado la práctica con éxito.", textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(12.dp))
                            BigButton(
                                text = "Siguiente Lección",
                                onClick = onFinish,
                                containerColor = Color(0xFF4CAF50)
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.sendCommand("S") },
                            modifier = Modifier.size(140.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                            enabled = isConnected
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.PanTool, contentDescription = null, modifier = Modifier.size(40.dp))
                                Text("PARAR", fontWeight = FontWeight.Black, fontSize = 20.sp)
                            }
                        }
                    }
                }

                // Pulgar Derecho: Izquierda y Derecha
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row() {

                        DirectionButton(
                            icon = Icons.Default.KeyboardArrowLeft,
                            onClick = { viewModel.sendCommand("L") },
                            enabled = isConnected && !practicaFinalizada,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        DirectionButton(
                            icon = Icons.Default.KeyboardArrowRight,
                            onClick = { viewModel.sendCommand("R") },
                            enabled = isConnected && !practicaFinalizada,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}
