package com.geka.sigem.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.geka.sigem.R
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate

@Composable
fun AnimatedSplashScreen(onFinish: () -> Unit) {

    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }
    val screenAlpha = remember { Animatable(1f) }

    val infiniteRotation = rememberInfiniteTransition()
    val rotation by infiniteRotation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    LaunchedEffect(Unit) {

        scale.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
        alpha.animateTo(1f, tween(900))

        delay(1500)

        screenAlpha.animateTo(0f, tween(700, easing = LinearEasing))

        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .alpha(screenAlpha.value),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale.value)
                    .alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(25.dp))

            Image(
                painter = painterResource(id = R.drawable.cargando),
                contentDescription = "Cargando...",
                modifier = Modifier
                    .size(45.dp)
                    .rotate(rotation)
                    .alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(10.dp))

            androidx.compose.material3.Text(
                text = "Cargando...",
                color = Color.Gray,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}
