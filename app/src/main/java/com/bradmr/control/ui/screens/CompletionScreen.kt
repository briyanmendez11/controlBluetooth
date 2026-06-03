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
 * Pantalla final que se muestra al completar todas las lecciones.
 */
@Composable
fun CompletionScreen(onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoCard(title = "¡Felicidades!") {
            Text(
                text = "Has completado todas las lecciones de educación vial. ¡Ahora eres un conductor experto!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Gracias por participar en este proyecto educativo.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        BigButton(
            text = "Volver al Inicio",
            onClick = onRestart,
            modifier = Modifier.width(300.dp)
        )
    }
}
