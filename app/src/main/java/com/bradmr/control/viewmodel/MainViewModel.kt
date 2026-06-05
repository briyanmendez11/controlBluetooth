package com.bradmr.control.viewmodel

import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bradmr.control.bluetooth.BluetoothController
import com.bradmr.control.data.model.Leccion
import com.bradmr.control.data.repository.LeccionesRepository
import com.bradmr.control.tts.TextToSpeechManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Enumeración para los modos de luces del carrito.
 */
enum class LightMode {
    NONE, LEFT, RIGHT, PARKING
}

/**
 * ViewModel principal que gestiona el estado de la aplicación, la lógica de las lecciones
 * y las interacciones con Bluetooth y TTS.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val bluetoothController = BluetoothController(application)
    private val ttsManager = TextToSpeechManager(application)

    // Estado de Bluetooth
    val connectionState: StateFlow<BluetoothController.ConnectionState> = bluetoothController.connectionState

    // Gestión de Lecciones
    private val _currentLeccionIndex = mutableStateOf(0)
    val currentLeccionIndex: State<Int> = _currentLeccionIndex

    val currentLeccion: Leccion
        get() = LeccionesRepository.lecciones[_currentLeccionIndex.value]

    private val _isLastLeccion = mutableStateOf(false)
    val isLastLeccion: State<Boolean> = _isLastLeccion

    // Estado de la lección actual
    private val _respuestaCorrectaDada = mutableStateOf(false)
    val respuestaCorrectaDada: State<Boolean> = _respuestaCorrectaDada

    private val _mensajeRespuesta = mutableStateOf("")
    val mensajeRespuesta: State<String> = _mensajeRespuesta

    private val _practicaFinalizada = mutableStateOf(false)
    val practicaFinalizada: State<Boolean> = _practicaFinalizada

    // --- Estado de Luces ---
    private val _lightMode = mutableStateOf(LightMode.NONE)
    val lightMode: State<LightMode> = _lightMode

    // --- Funciones Bluetooth ---

    fun getPairedDevices(): List<BluetoothDevice> = bluetoothController.getPairedDevices()

    fun connectToDevice(device: BluetoothDevice) {
        viewModelScope.launch {
            bluetoothController.connectToDevice(device)
        }
    }

    fun sendCommand(command: String) {
        bluetoothController.sendCommand(command)
    }

    // --- Lógica de Luces ---
    
    fun toggleLight(mode: LightMode) {
        if (_lightMode.value == mode) {
            // Si ya está activo, apagar
            _lightMode.value = LightMode.NONE
            sendCommand("M")
        } else {
            // Activar nuevo modo
            _lightMode.value = mode
            val command = when (mode) {
                LightMode.LEFT -> "1"
                LightMode.RIGHT -> "2"
                LightMode.PARKING -> "3"
                else -> "M"
            }
            sendCommand(command)
        }
    }

    // --- Funciones TTS ---

    fun hablar(texto: String) {
        ttsManager.speak(texto)
    }

    fun detenerVoz() {
        ttsManager.stop()
    }

    // --- Lógica de Lecciones ---

    fun verificarRespuesta(opcion: String) {
        if (opcion == currentLeccion.respuestaCorrecta) {
            _respuestaCorrectaDada.value = true
            _mensajeRespuesta.value = "¡Correcto! Muy bien hecho."
        } else {
            _mensajeRespuesta.value = "Incorrecto. Inténtalo de nuevo."
        }
    }

    fun finalizarPractica() {
        _practicaFinalizada.value = true
        sendCommand("S") // Detener el carrito al finalizar
        sendCommand("M") // Apagar luces al finalizar
        _lightMode.value = LightMode.NONE
    }

    fun siguienteLeccion() {
        if (_currentLeccionIndex.value < LeccionesRepository.lecciones.size - 1) {
            _currentLeccionIndex.value++
            resetEstadoLeccion()
        } else {
            _isLastLeccion.value = true
        }
    }

    private fun resetEstadoLeccion() {
        _respuestaCorrectaDada.value = false
        _mensajeRespuesta.value = ""
        _practicaFinalizada.value = false
        _lightMode.value = LightMode.NONE
        detenerVoz()
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.closeConnection()
        ttsManager.release()
    }
}
