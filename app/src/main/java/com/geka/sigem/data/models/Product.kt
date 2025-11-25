package com.geka.sigem.data.models


// 1. El nivel más profundo: Datos del Empleado
data class Empleado(
    val idEmpleado: Int,
    val nombre: String,
    val apellido: String,
    val direccion: String,
    val colonia: String,
    val telefonoPersonal: String,
    val correoPersonal: String,
    val extension: String?,        // Puede ser null o vacío
    val correoInstitucional: String,
    val fechaIngreso: String,
    val fechaSalida: String?       // En tu JSON viene como null, así que debe ser nullable
)

// 2. El objeto Usuario que contiene al Empleado
data class UsuarioFull(
    val id: Int,
    val usuario: String,
    val contrasenia: String,
    val empleado: Empleado         // Objeto anidado
)

// 3. La Publicación que contiene al Usuario
data class Publicacion(
    val idPublicacion: Int,
    val producto: String,
    val precio: String,
    val descripcion: String,
    val estatus: String,
    val usuario: UsuarioFull,      // Aquí conectamos el objeto completo
    val fotos: List<Foto>
)

// 4. Las Fotos (se mantiene igual)
data class Foto(
    val idFoto: Int,
    val link: String,
    val estatus: String,
    val idPublicacion: Int
)