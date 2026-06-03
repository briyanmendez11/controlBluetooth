package com.bradmr.control

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bradmr.control.navigation.NavGraph
import com.bradmr.control.ui.theme.ControlTheme
import com.bradmr.control.viewmodel.MainViewModel

/**
 * Actividad principal del proyecto de Educación Vial.
 * Configura el tema, habilita el diseño edge-to-edge e inicia el flujo de navegación.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Mantenemos la pantalla encendida durante la demo para facilitar el control del carrito
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        enableEdgeToEdge()
        
        setContent {
            ControlTheme {
                // Instanciamos el ViewModel que manejará el estado global de la app
                val viewModel: MainViewModel = viewModel()
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Iniciamos el grafo de navegación pasando el ViewModel
                    NavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
