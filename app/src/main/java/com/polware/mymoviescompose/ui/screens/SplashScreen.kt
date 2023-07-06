package com.polware.mymoviescompose.ui.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.polware.mymoviescompose.R
import com.polware.mymoviescompose.ui.theme.topAppBarBackgroundColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToListScreen: () -> Unit
) {
    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 0.9f,
            animationSpec = tween(durationMillis = 1000,
                easing = {
                    OvershootInterpolator(8f).getInterpolation(it)
                })
        )
        delay(3000L)
        navigateToListScreen()
    }
    Surface(
        modifier = Modifier
            .scale(scale.value)
            .padding(12.dp)
            .size(340.dp),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 3.dp,
            color = MaterialTheme.colors.topAppBarBackgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.padding(2.dp),
                shape = CircleShape,
                elevation = 5.dp
            ) {
                AsyncImage(
                    model = R.drawable.applogo,
                    contentScale = ContentScale.Crop,
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(2.dp)
                )
            }
            Text(text = "My Movies",
                color = MaterialTheme.colors.topAppBarBackgroundColor,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )
        }
    }

}
