package com.geka.sigem.data.models

data class LoginResponse(
    val idUser: Int,
    val idEmpleado: Int?,
    val access_token: String,
    val usuario: String,
    val rol: String?
)

