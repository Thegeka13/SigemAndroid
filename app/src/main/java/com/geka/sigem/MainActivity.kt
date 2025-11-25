package com.geka.sigem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geka.sigem.ui.theme.SigemTheme
import com.geka.sigem.ui.viewmodel.AuthViewModel
import com.geka.sigem.AppNavHost
import dagger.hilt.android.AndroidEntryPoint // ⬅️ Importación necesaria

// ¡Esta anotación es la única necesaria aquí para habilitar Hilt en el ámbito de la Activity!
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SigemTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Mantenemos la creación estándar, ya que AuthViewModel no usa inyección
                    val authViewModel: AuthViewModel = viewModel()

                    // AppNavHost usará hiltViewModel() para el MarketplaceViewModel
                    AppNavHost(authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SigemTheme {
        Greeting("Android")
    }
}