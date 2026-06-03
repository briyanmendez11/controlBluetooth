package com.bradmr.control.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * Clase para gestionar la funcionalidad de Texto a Voz (TTS).
 */
class TextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "ES"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "El idioma español no está soportado")
            } else {
                isInitialized = true
            }
        } else {
            Log.e("TTS", "Error al inicializar TTS")
        }
    }

    /**
     * Reproduce el texto proporcionado.
     */
    fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    /**
     * Detiene la reproducción actual.
     */
    fun stop() {
        tts?.stop()
    }

    /**
     * Libera los recursos del motor TTS.
     */
    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
