package com.geka.sigem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.Publicacion
import com.geka.sigem.data.repository.MarketplaceRepository
import com.geka.sigem.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val repo: MarketplaceRepository,
    private val sessionManager: SessionManager // Inyectamos el SessionManager
) : ViewModel() {

    // ---------------------------
    // ESTADOS (StateFlow)
    // ---------------------------
    private val _publicaciones = MutableStateFlow<List<Publicacion>>(emptyList())
    val publicaciones: StateFlow<List<Publicacion>> = _publicaciones

    private val _misPublicaciones = MutableStateFlow<List<Publicacion>>(emptyList())
    val misPublicaciones: StateFlow<List<Publicacion>> = _misPublicaciones

    private val _detalle = MutableStateFlow<Publicacion?>(null)
    val detalle: StateFlow<Publicacion?> = _detalle

    // ===========================
    // CARGAR TODAS LAS PUBLICACIONES
    // ===========================
    fun cargarPublicaciones() {
        viewModelScope.launch {
            try {
                val resultado = withContext(Dispatchers.IO) {
                    repo.getPublicaciones()
                }
                _publicaciones.value = resultado
            } catch (e: Exception) {
                e.printStackTrace()
                _publicaciones.value = emptyList()
            }
        }
    }

    // ===========================
    // CARGAR SOLO MIS PUBLICACIONES
    // ===========================
    fun cargarMisPublicaciones() {
        viewModelScope.launch {
            // Obtenemos el ID de la sesión interna
            val idUsuario = sessionManager.getUserId()

            if (idUsuario == -1) {
                _misPublicaciones.value = emptyList()
                return@launch
            }

            try {
                val resultado = withContext(Dispatchers.IO) {
                    repo.getPublicacionesDeUsuario(idUsuario)
                }
                _misPublicaciones.value = resultado
            } catch (e: Exception) {
                e.printStackTrace()
                _misPublicaciones.value = emptyList()
            }
        }
    }

    // ===========================
    // DETALLE
    // ===========================
    fun cargarPublicacion(id: Int) {
        viewModelScope.launch {
            try {
                val resultado = withContext(Dispatchers.IO) {
                    repo.getPublicacion(id)
                }
                _detalle.value = resultado
            } catch (e: Exception) {
                e.printStackTrace()
                _detalle.value = null
            }
        }
    }

    // ===========================
    // CREAR PUBLICACIÓN (MODIFICADO)
    // ===========================
    // Ya no recibe idUsuario como parámetro, lo saca del SessionManager
    fun crearPublicacion(
        producto: String,
        precio: String,
        descripcion: String,
        imagenes: List<File>,
        onFinish: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            // 1. Validar sesión
            val idUsuario = sessionManager.getUserId()

            if (idUsuario == -1) {
                // Si no hay sesión válida, fallamos
                onFinish(false)
                return@launch
            }

            // Estatus por defecto activo
            val estatus = 1

            try {
                // 2. Llamada al repositorio en hilo IO
                withContext(Dispatchers.IO) {
                    repo.crearPublicacion(
                        producto = producto,
                        precio = precio,
                        descripcion = descripcion,
                        estatus = estatus,
                        idUsuario = idUsuario, // Usamos el ID obtenido del token/sesión
                        imagenes = imagenes
                    )
                }

                // 3. Recargar listas para actualizar la UI
                cargarPublicaciones()
                cargarMisPublicaciones()

                onFinish(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onFinish(false)
            }
        }
    }

    // ===========================
    // ACTUALIZAR PUBLICACIÓN
    // ===========================
    fun actualizarPublicacion(
        id: Int,
        producto: String,
        precio: String,
        descripcion: String,
        estatus: Int = 1,
        fotosAEliminar: List<Int>,
        nuevasImagenes: List<File>,
        onFinish: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repo.actualizarPublicacion(
                        id,
                        producto,
                        precio,
                        descripcion,
                        estatus,
                        fotosAEliminar,
                        nuevasImagenes
                    )
                }
                cargarPublicaciones()
                cargarMisPublicaciones()
                onFinish(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onFinish(false)
            }
        }
    }
}