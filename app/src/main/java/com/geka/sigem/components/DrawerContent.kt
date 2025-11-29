package com.geka.sigem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    onMarket: () -> Unit,
    onCursos: () -> Unit,   // ← Nuevo callback
    onApoyos: () -> Unit,
    onCursos: () -> Unit,
    onSolicitudes: () -> Unit,
    onEventos: () -> Unit,  // ← NUEVO CALLBACK
    onLogout: () -> Unit
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {

            Text(
                text = "Menú",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            Divider()

            NavigationDrawerItem(
                label = { Text("Market") },
                icon = { Icon(Icons.Default.ShoppingCart, "Market") },
                selected = false,
                onClick = { onMarket() },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            NavigationDrawerItem(
                label = { Text("Cursos") },
                icon = { Icon(Icons.Default.School, "Cursos") },
                selected = false,
                onClick = { onCursos() },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            //Apoyos
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.CardGiftcard, contentDescription = null) }, // 👈 Cambia aquí
                label = { Text("Apoyos") },
                selected = false,
                onClick = onApoyos
            )


            // ✅ NUEVO BOTÓN DE EVENTOS
            NavigationDrawerItem(
                label = { Text("Eventos") },
                icon = { Icon(Icons.Default.Event, "Eventos") },
                selected = false,
                onClick = { onEventos() },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            NavigationDrawerItem(
                label = { Text("Solicitudes") },
                icon = { Icon(Icons.Default.CalendarToday, "Solicitudes") },
                selected = false,
                onClick = { onSolicitudes() },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            NavigationDrawerItem(
                label = { Text("Logout") },
                icon = { Icon(Icons.Default.Logout, "Logout") },
                selected = false,
                onClick = { onLogout() },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}
