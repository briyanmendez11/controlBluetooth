package com.bradmr.control.data.model

/**
 * Representa una lección de educación vial.
 */
data class Leccion(
    val id: Int,
    val titulo: String,
    val textoCorto: String,
    val textoAudio: String,
    val pregunta: String,
    val opciones: List<String>,
    val respuestaCorrecta: String,
    val instruccionPractica: String
)
