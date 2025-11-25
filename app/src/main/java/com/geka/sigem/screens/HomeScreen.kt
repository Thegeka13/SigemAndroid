package com.geka.sigem.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    // Mantenemos estos parámetros para que coincida con la llamada en AppNavHost,
    // pero ya no sirven para el menú lateral, sirven si quieres botones en el centro.
    onNavigateToSolicitudes: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToCursos: () -> Unit,
    onLogout: () -> Unit
) {
    // YA NO usamos MainLayout ni Scaffold.
    // El AppNavHost se encarga de todo el marco.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido a Sigem",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Selecciona una opción del menú o usa los accesos directos:",
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Ejemplo: Botones de acceso rápido en el Home
        Button(onClick = onNavigateToMarket) {
            Text("Ir al Marketplace")
        }
    }
}