package com.geka.sigem.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.geka.sigem.components.EventoCard
import com.geka.sigem.viewmodel.EventoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventosScreen(
    viewModel: EventoViewModel,
    onOpenDetail: (Int) -> Unit
) {
    val eventos = viewModel.eventos
    val loading = viewModel.loading

    LaunchedEffect(Unit) {
        viewModel.cargarEventos()
    }

    Scaffold { padding ->

        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            when {
                loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                eventos == null -> Unit

                else -> {
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(eventos!!) { evento ->
                            EventoCard(evento = evento) {
                                onOpenDetail(evento.idEvento)
                            }
                        }
                    }
                }
            }
        }
    }
}
