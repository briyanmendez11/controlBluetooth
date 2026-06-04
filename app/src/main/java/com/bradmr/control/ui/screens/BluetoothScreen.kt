package com.bradmr.control.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bradmr.control.bluetooth.BluetoothController
import com.bradmr.control.ui.components.BigButton
import com.bradmr.control.ui.components.InfoCard
import com.bradmr.control.viewmodel.MainViewModel

/**
 * Pantalla para gestionar la conexión Bluetooth con el carrito.
 * Mejorada visualmente para ser más atractiva y clara para los niños.
 */
@SuppressLint("MissingPermission")
@Composable
fun BluetoothScreen(viewModel: MainViewModel, onContinue: () -> Unit) {
    val context = LocalContext.current
    val connectionState by viewModel.connectionState.collectAsState()
    val pairedDevices = remember { mutableStateListOf<BluetoothDevice>() }

    // Manejo de permisos
    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
    } else {
        arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            pairedDevices.clear()
            pairedDevices.addAll(viewModel.getPairedDevices())
        }
    }

    LaunchedEffect(Unit) {
        if (permissionsToRequest.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
            pairedDevices.addAll(viewModel.getPairedDevices())
        } else {
            launcher.launch(permissionsToRequest)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // Lado izquierdo: Estado y lista de dispositivos
        Column(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight()
        ) {
            InfoCard(
                title = "Conecta tu Carrito",
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (connectionState is BluetoothController.ConnectionState.Connected) 
                            Icons.Default.BluetoothConnected else Icons.Default.Bluetooth,
                        contentDescription = null,
                        tint = if (connectionState is BluetoothController.ConnectionState.Connected) 
                            MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = when (connectionState) {
                            is BluetoothController.ConnectionState.Disconnected -> "Estado: Esperando conexión..."
                            is BluetoothController.ConnectionState.Connecting -> "Estado: Conectando..."
                            is BluetoothController.ConnectionState.Connected -> "Estado: ¡Listo para jugar!"
                            is BluetoothController.ConnectionState.Error -> "Error: Algo salió mal"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Toca el nombre del carrito (HC-05):",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    items(pairedDevices) { device ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { viewModel.connectToDevice(device) },
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Bluetooth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        device.name ?: "Carrito Desconocido",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(device.address, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        // Lado derecho: Acción de continuar e ilustración de ayuda
        Column(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*InfoCard(
                title = "¿Necesitas ayuda?",
                containerColor = MaterialTheme.colorScheme.inverseSurface
            ) {
                Text(
                    "Busca el dispositivo llamado HC-05 en la lista y selecciónalo.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }*/
            
            Spacer(modifier = Modifier.height(32.dp))
            
            BigButton(
                text = "¡Aprender y Jugar!",
                onClick = onContinue,
                enabled = connectionState is BluetoothController.ConnectionState.Connected,
                containerColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth()
            )
            
            if (connectionState !is BluetoothController.ConnectionState.Connected) {
                Text(
                    text = "Conéctate para empezar",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
