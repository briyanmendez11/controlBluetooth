package com.bradmr.control.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

/**
 * Clase encargada de gestionar la conexión Bluetooth con el módulo HC-05.
 */
class BluetoothController(private val context: Context) {

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    sealed class ConnectionState {
        object Disconnected : ConnectionState()
        object Connecting : ConnectionState()
        data class Connected(val deviceName: String) : ConnectionState()
        data class Error(val message: String) : ConnectionState()
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices(): List<BluetoothDevice> {
        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
    }

    @SuppressLint("MissingPermission")
    suspend fun connectToDevice(device: BluetoothDevice) {
        _connectionState.value = ConnectionState.Connecting
        withContext(Dispatchers.IO) {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
                bluetoothAdapter?.cancelDiscovery()
                bluetoothSocket?.connect()
                outputStream = bluetoothSocket?.outputStream
                _connectionState.value = ConnectionState.Connected(device.name ?: "Desconocido")
            } catch (e: IOException) {
                Log.e("BluetoothController", "Error al conectar", e)
                _connectionState.value = ConnectionState.Error("No se pudo conectar: ${e.message}")
                closeConnection()
            }
        }
    }

    /**
     * Envía un comando de texto al Arduino.
     * Mejorado para forzar el envío inmediato de bytes ASCII.
     */
    fun sendCommand(command: String) {
        val socket = bluetoothSocket
        val out = outputStream
        if (socket != null && socket.isConnected && out != null) {
            try {
                // Convertimos a ASCII (1 byte por carácter) para el Arduino
                val data = command.toByteArray(Charsets.US_ASCII)
                out.write(data)
                out.flush() // Forzar salida inmediata
                Log.d("BT_CMD", "Enviado a Arduino: $command")
            } catch (e: IOException) {
                Log.e("BT_CMD", "Error al enviar comando: $command", e)
            }
        } else {
            Log.w("BT_CMD", "No se envió '$command' porque el socket no está listo")
        }
    }

    fun closeConnection() {
        try {
            outputStream?.close()
            bluetoothSocket?.close()
        } catch (e: IOException) {
            Log.e("BluetoothController", "Error al cerrar conexión", e)
        } finally {
            bluetoothSocket = null
            outputStream = null
            _connectionState.value = ConnectionState.Disconnected
        }
    }
}
