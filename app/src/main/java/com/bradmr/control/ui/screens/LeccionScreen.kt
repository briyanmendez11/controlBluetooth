package com.bradmr.control.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bradmr.control.ui.components.BigButton
import com.bradmr.control.ui.components.InfoCard
import com.bradmr.control.viewmodel.MainViewModel

/**
 * Pantalla de lección optimizada para modo horizontal.
 * Distribuye el contenido de forma clara y atractiva para niños.
 */
@Composable
fun LeccionScreen(
    viewModel: MainViewModel,
    onNavigateToControl: () -> Unit
) {
    val leccion = viewModel.currentLeccion
    val respuestaCorrectaDada = viewModel.respuestaCorrectaDada.value
    val mensajeRespuesta = viewModel.mensajeRespuesta.value

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // Lado izquierdo: Explicación y Audio
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InfoCard(
                title = leccion.titulo,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = leccion.textoCorto,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.hablar(leccion.textoAudio) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Escuchar")
                    }
                    
                    Button(
                        onClick = { viewModel.detenerVoz() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Detener")
                    }
                }
            }
        }

        // Lado derecho: Evaluación y Acción
        Column(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxHeight()
                .padding(start = 1.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InfoCard(
                title = "¡Demuestra lo aprendido!",
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = leccion.pregunta,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Opciones de respuesta
                leccion.opciones.forEach { opcion ->
                    val isCorrectSelection = respuestaCorrectaDada && opcion == leccion.respuestaCorrecta
                    
                    Button(
                        onClick = { viewModel.verificarRespuesta(opcion) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCorrectSelection) Color(0xFF4F86AB) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isCorrectSelection) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(text = opcion, fontWeight = FontWeight.Medium)
                    }
                }

                if (mensajeRespuesta.isNotEmpty()) {
                    Text(
                        text = mensajeRespuesta,
                        color = if (respuestaCorrectaDada) Color(0xFF388E3C) else MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de práctica centrado y habilitado solo al responder bien
            BigButton(
                text = "¡A conducir el carrito!",
                onClick = onNavigateToControl,
                enabled = true,//respuestaCorrectaDada,
                containerColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth()
            )
            
            if (!respuestaCorrectaDada) {
                Text(
                    text = "Responde correctamente para desbloquear la práctica",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }
    }
}
