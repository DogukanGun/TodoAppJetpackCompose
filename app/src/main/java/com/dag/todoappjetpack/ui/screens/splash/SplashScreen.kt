package com.dag.todoappjetpack.ui.screens.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.dag.todoappjetpack.ui.theme.LOGO_SIZE
import com.dag.todoappjetpack.ui.theme.splashScreenBackground
import com.dag.todoappjetpack.util.Constant
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToTaskScreen: () -> Unit
){
    var splashScreenAnimation by remember { mutableStateOf(false) }
    val offSetState by animateDpAsState(
        targetValue = if (splashScreenAnimation) 0.dp else 100.dp,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    val alphaState by animateFloatAsState(
        targetValue = if (splashScreenAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    LaunchedEffect(key1 = true){
        splashScreenAnimation = true
        delay(Constant.SPLASH_SCREEN_DELAY)
        navigateToTaskScreen()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.splashScreenBackground),
        contentAlignment = Alignment.Center
    ){
        Image(
            modifier = Modifier
                .size(LOGO_SIZE)
                .offset(y = offSetState)
                .alpha(alpha = alphaState),
            imageVector = Icons.Filled.Star ,
            contentDescription = "logo"
        )
    }
}

@Composable
fun GetLogo():Int{
    //buraya logonun id sini getir
    return 0
}