package com.bradmr.control.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bradmr.control.ui.components.BigButton
import com.bradmr.control.ui.components.InfoCard
import com.bradmr.control.viewmodel.MainViewModel

/**
 * Pantalla que muestra el contenido de una lección y una pregunta de evaluación.
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
            .padding(16.dp)
    ) {
        // Lado izquierdo: Contenido de la lección
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InfoCard(title = leccion.titulo) {
                Text(text = leccion.textoCorto, style = MaterialTheme.typography.bodyLarge)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row {
                    BigButton(
                        text = "Escuchar",
                        onClick = { viewModel.hablar(leccion.textoAudio) },
                        modifier = Modifier.weight(1f).padding(end = 4.dp),
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                    BigButton(
                        text = "Detener",
                        onClick = { viewModel.detenerVoz() },
                        modifier = Modifier.weight(1f).padding(start = 4.dp),
                        containerColor = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(title = "Pregunta") {
                Text(text = leccion.pregunta, style = MaterialTheme.typography.titleMedium)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                leccion.opciones.forEach { opcion ->
                    BigButton(
                        text = opcion,
                        onClick = { viewModel.verificarRespuesta(opcion) },
                        containerColor = if (respuestaCorrectaDada && opcion == leccion.respuestaCorrecta) 
                            Color(0xFF4CAF50) else MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                if (mensajeRespuesta.isNotEmpty()) {
                    Text(
                        text = mensajeRespuesta,
                        color = if (respuestaCorrectaDada) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // Lado derecho: Acción de práctica
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BigButton(
                text = "Practicar con el carrito",
                onClick = onNavigateToControl,
                enabled = respuestaCorrectaDada,
                containerColor = MaterialTheme.colorScheme.tertiary
            )
            
            if (!respuestaCorrectaDada) {
                Text(
                    text = "Responde correctamente para practicar.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
