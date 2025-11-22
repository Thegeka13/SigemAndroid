package com.geka.sigem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.ListItem
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.DrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    onMarket: () -> Unit,
    onLogout: () -> Unit
) {
    DrawerSheet {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = "Menú",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            Divider()
            ListItem(
                headlineContent = { Text("Market") },
                leadingContent = { Icon(Icons.Default.ShoppingCart, contentDescription = "Market") },
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingContent = {},
                onClick = { onMarket() }
            )
            ListItem(
                headlineContent = { Text("Logout") },
                leadingContent = { Icon(Icons.Default.Logout, contentDescription = "Logout") },
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = { onLogout() }
            )
        }
    }
}
