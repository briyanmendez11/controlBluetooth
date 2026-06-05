package com.bradmr.control.ui.components

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tarjeta estilizada para niños con bordes redondeados.
 */
@Composable
fun InfoCard(
    title: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

/**
 * Botón principal de la aplicación.
 */
@Composable
fun BigButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

/**
 * Botón "Mantenido": Envía un comando al presionar y "S" al soltar.
 * Usa InteractionSource para detectar el estado físico del dedo en la pantalla.
 */
@Composable
fun HoldToMoveButton(
    icon: ImageVector,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Evita enviar el comando "S" la primera vez que se carga el componente
    var wasPressed by remember { mutableStateOf(false) }

    LaunchedEffect(isPressed) {
        if (enabled) {
            if (isPressed) {
                wasPressed = true
                Log.d("HoldToMoveButton", "Presionado: Enviando comando de movimiento")
                onPress()
            } else if (wasPressed) {
                // Solo envía el comando de parada si antes se presionó el botón
                Log.d("HoldToMoveButton", "Soltado: Enviando 'S'")
                onRelease()
                wasPressed = false
            }
        }
    }

    FilledIconButton(
        onClick = { /* No se usa el click simple */ },
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier
            .size(90.dp, 75.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = color,
            contentColor = Color.White
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(44.dp))
    }
}

/**
 * Botón para luces con indicador de estado (Amarillo al activar).
 */
@Composable
fun LightToggleButton(
    icon: ImageVector,
    text: String,
    isActive: Boolean,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFFFFD600)
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(50.dp).padding(2.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) activeColor else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isActive) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * Botón de dirección para giros (Izquierda/Derecha).
 */
@Composable
fun DirectionButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(75.dp),
        shape = RoundedCornerShape(16.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = color,
            contentColor = Color.White
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(36.dp))
    }
}
