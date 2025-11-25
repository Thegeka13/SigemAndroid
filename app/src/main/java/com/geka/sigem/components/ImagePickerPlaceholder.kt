package com.geka.sigem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImagePickerPlaceholder(onPick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Button(onClick = onPick, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar Fotos")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Las imágenes aparecerán aquí (placeholder).")
    }
}