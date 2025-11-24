package com.geka.sigem

import android.app.Application
import dagger.hilt.android.HiltAndroidApp // ⬅️ Nueva importación

// Clase de inicio de Hilt
@HiltAndroidApp
class SigemApplication : Application() {
    // Hilt inicializa todo aquí
}