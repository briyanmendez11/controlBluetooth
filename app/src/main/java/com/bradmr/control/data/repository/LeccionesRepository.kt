package com.bradmr.control.data.repository

import com.bradmr.control.data.model.Leccion

/**
 * Repositorio que contiene las lecciones del proyecto.
 */
object LeccionesRepository {
    val lecciones = listOf(
        Leccion(
            id = 1,
            titulo = "Lección 1: El semáforo",
            textoCorto = "Rojo significa ALTO. Amarillo significa CUIDADO. Verde significa AVANZA.",
            textoAudio = "El semáforo nos ayuda a conducir con seguridad. Cuando está en rojo debemos detenernos. Cuando está en amarillo debemos tener cuidado porque va a cambiar. Cuando está en verde podemos avanzar.",
            pregunta = "¿Qué debes hacer si el semáforo está en rojo?",
            opciones = listOf("Avanzar rápido", "Detenerme", "Girar sin mirar"),
            respuestaCorrecta = "Detenerme",
            instruccionPractica = "Practica deteniendo el carrito cuando imagines que el semáforo está en rojo."
        ),
        Leccion(
            id = 2,
            titulo = "Lección 2: Paso peatonal",
            textoCorto = "El paso peatonal es el lugar seguro para que las personas crucen la calle.",
            textoAudio = "El paso peatonal, también llamado cebra, es el lugar donde las personas pueden cruzar con mayor seguridad. El vehículo debe detenerse antes del paso peatonal.",
            pregunta = "¿Dónde deben cruzar las personas?",
            opciones = listOf("Por cualquier parte de la calle", "Por el paso peatonal", "Por detrás de los autos"),
            respuestaCorrecta = "Por el paso peatonal",
            instruccionPractica = "Conduce el carrito y detente antes de llegar a un paso peatonal imaginario."
        ),
        Leccion(
            id = 3,
            titulo = "Lección 3: Carril correcto",
            textoCorto = "Los vehículos deben avanzar por su carril y no invadir el carril contrario.",
            textoAudio = "Cuando conducimos debemos mantenernos en nuestro carril. No debemos cruzar al carril contrario porque puede ser peligroso.",
            pregunta = "¿Qué debes hacer al conducir?",
            opciones = listOf("Ir por mi carril", "Invadir el otro carril", "Manejar en zigzag"),
            respuestaCorrecta = "Ir por mi carril",
            instruccionPractica = "Mueve el carrito hacia adelante intentando mantener una línea recta."
        ),
        Leccion(
            id = 4,
            titulo = "Lección 4: Guiñadores",
            textoCorto = "Antes de girar, debemos avisar usando la luz direccional.",
            textoAudio = "Antes de girar a la izquierda o a la derecha debemos avisar con el guiñador. Así las demás personas saben qué movimiento vamos a realizar.",
            pregunta = "¿Qué debes hacer antes de girar?",
            opciones = listOf("Usar el guiñador", "Girar sin avisar", "Apagar el carrito"),
            respuestaCorrecta = "Usar el guiñador",
            instruccionPractica = "Gira el carrito a la izquierda y a la derecha para practicar los giros."
        ),
        Leccion(
            id = 5,
            titulo = "Lección 5: Cuidado con los peligros",
            textoCorto = "Si ves conos, una obra o algo en el camino, debes ir despacio, mirar bien y detenerte si es necesario.",
            textoAudio = "Cuando vemos conos, una obra o un obstáculo en el camino, debemos tener mucho cuidado. No debemos acelerar ni pasar sin mirar. Debemos bajar la velocidad, respetar las señales y detenernos si hay peligro.",
            pregunta = "¿Qué debes hacer si ves conos u obstáculos en el camino?",
            opciones = listOf(
                "Ir despacio y tener cuidado",
                "Acelerar para pasar rápido",
                "Chocar los conos"
            ),
            respuestaCorrecta = "Ir despacio y tener cuidado",
            instruccionPractica = "Conduce despacio y evita los conos u obstáculos de la pista."
        )
    )
}
