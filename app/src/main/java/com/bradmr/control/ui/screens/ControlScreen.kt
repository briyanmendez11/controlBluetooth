package com.bradmr.control.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bradmr.control.bluetooth.BluetoothController
import com.bradmr.control.ui.components.*
import com.bradmr.control.viewmodel.LightMode
import com.bradmr.control.viewmodel.MainViewModel

/**
 * Pantalla de control remoto optimizada para manejo con dos manos y control de luces.
 * Todos los botones de movimiento (incluyendo giros) envían 'S' al soltarse para mayor precisión.
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
    val lightMode by viewModel.lightMode

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        // Barra Superior: Navegación y Estado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
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
                containerColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.height(45.dp)
            )
        }

        if (!practicaFinalizada) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // COLUMNA IZQUIERDA: Luces de giro e Intermitentes y Avance/Retroceso
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // BOTONES DE LUCES IZQUIERDA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LightToggleButton(
                            icon = Icons.Default.ArrowBack,
                            text = "Izq",
                            isActive = lightMode == LightMode.LEFT,
                            onClick = { viewModel.toggleLight(LightMode.LEFT) },
                            enabled = isConnected,
                            modifier = Modifier.weight(1f)
                        )
                        LightToggleButton(
                            icon = Icons.Default.ArrowForward,
                            text = "Der",
                            isActive = lightMode == LightMode.RIGHT,
                            onClick = { viewModel.toggleLight(LightMode.RIGHT) },
                            enabled = isConnected,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    // MOVIMIENTO ADELANTE/ATRÁS (HOLD-TO-MOVE)
                    HoldToMoveButton(
                        icon = Icons.Default.KeyboardArrowUp,
                        onPress = { viewModel.sendCommand("F") },
                        onRelease = { viewModel.sendCommand("S") },
                        enabled = isConnected,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HoldToMoveButton(
                        icon = Icons.Default.KeyboardArrowDown,
                        onPress = { viewModel.sendCommand("B") },
                        onRelease = { viewModel.sendCommand("S") },
                        enabled = isConnected,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // CENTRO: Instrucción
                Box(
                    modifier = Modifier.weight(0.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = leccion.instruccionPractica,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }

                // COLUMNA DERECHA: Parqueo y Dirección (Ahora también Hold-To-Move para precisión)
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // BOTÓN PARQUEO
                    LightToggleButton(
                        icon = Icons.Default.Warning,
                        text = "Luces de Parqueo",
                        isActive = lightMode == LightMode.PARKING,
                        onClick = { viewModel.toggleLight(LightMode.PARKING) },
                        enabled = isConnected,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // DIRECCIÓN IZQUIERDA/DERECHA (HOLD-TO-MOVE)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                    ) {
                        HoldToMoveButton(
                            icon = Icons.Default.KeyboardArrowLeft,
                            onPress = { viewModel.sendCommand("L") },
                            onRelease = { viewModel.sendCommand("S") },
                            enabled = isConnected && !practicaFinalizada,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        HoldToMoveButton(
                            icon = Icons.Default.KeyboardArrowRight,
                            onPress = { viewModel.sendCommand("R") },
                            onRelease = { viewModel.sendCommand("S") },
                            enabled = isConnected && !practicaFinalizada,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        } else {
            // Éxito al finalizar
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                InfoCard(
                    title = "¡Excelente!",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.width(350.dp)
                ) {
                    Text("Has terminado la práctica. ¡Vamos a la siguiente lección!", textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    BigButton(
                        text = "Continuar",
                        onClick = onFinish,
                        containerColor = Color(0xFF4CAF50),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
