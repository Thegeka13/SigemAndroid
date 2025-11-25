// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // --- IMPORTACIÓN DE HILT FALTANTE ---
    // Agrega el plugin de Hilt con la versión
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
    // ------------------------------------
}