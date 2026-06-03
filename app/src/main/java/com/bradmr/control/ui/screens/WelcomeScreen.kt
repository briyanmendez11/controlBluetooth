package com.bradmr.control.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bradmr.control.ui.components.BigButton
import com.bradmr.control.ui.components.InfoCard

/**
 * Pantalla de bienvenida que explica el propósito de la app.
 */
@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoCard(title = "¡Bienvenido a Educación Vial!") {
                Text(
                    text = "Aprende las reglas de tránsito, escucha los consejos y practica conduciendo tu carrito educativo por Bluetooth.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.width(32.dp))

        Column(
            modifier = Modifier.weight(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BigButton(
                text = "Comenzar",
                onClick = onStartClick
            )
        }
    }
}
