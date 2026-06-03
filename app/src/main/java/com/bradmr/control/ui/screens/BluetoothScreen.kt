package com.bradmr.control.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bradmr.control.bluetooth.BluetoothController
import com.bradmr.control.ui.components.BigButton
import com.bradmr.control.ui.components.InfoCard
import com.bradmr.control.viewmodel.MainViewModel

/**
 * Pantalla para gestionar la conexión Bluetooth con el carrito.
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
            .padding(16.dp)
    ) {
        // Lado izquierdo: Estado y lista de dispositivos
        Column(
            modifier = Modifier.weight(1f).padding(8.dp)
        ) {
            InfoCard(title = "Conexión al Carrito") {
                Text(
                    text = when (connectionState) {
                        is BluetoothController.ConnectionState.Disconnected -> "Estado: Desconectado"
                        is BluetoothController.ConnectionState.Connecting -> "Estado: Conectando..."
                        is BluetoothController.ConnectionState.Connected -> "Estado: Conectado a ${(connectionState as BluetoothController.ConnectionState.Connected).deviceName}"
                        is BluetoothController.ConnectionState.Error -> "Error: ${(connectionState as BluetoothController.ConnectionState.Error).message}"
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Dispositivos Emparejados:", style = MaterialTheme.typography.titleMedium)
            
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(pairedDevices) { device ->
                    ListItem(
                        headlineContent = { Text(device.name ?: "Dispositivo sin nombre") },
                        supportingContent = { Text(device.address) },
                        modifier = Modifier.clickable {
                            viewModel.connectToDevice(device)
                        }
                    )
                    HorizontalDivider()
                }
            }
        }

        // Lado derecho: Acción de continuar
        Column(
            modifier = Modifier.weight(0.5f).fillMaxHeight().padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BigButton(
                text = "Continuar",
                onClick = onContinue,
                enabled = connectionState is BluetoothController.ConnectionState.Connected
            )
            
            if (connectionState !is BluetoothController.ConnectionState.Connected) {
                Text(
                    text = "Por favor, conéctate al módulo HC-05 para continuar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
