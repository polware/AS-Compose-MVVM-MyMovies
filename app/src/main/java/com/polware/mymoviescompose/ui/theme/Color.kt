package com.polware.mymoviescompose.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Red = Color(0xFFF70707)
val Purple700 = Color(0xFF3700B3)
val LightGray = Color(0xFFFCFCFC)
val Orange = Color(0xFFEC5D35)
val MediumGray = Color(0xFF9C9C9C)
val DarkGray = Color(0xFF141414)

val Colors.fabBackgroundColor: Color
    @Composable
    get() = if (isLight) Red else Purple700

val Colors.topAppBarContentColor: Color
    @Composable
    get() = if (isLight) Color.White else LightGray

val Colors.topAppBarBackgroundColor: Color
    @Composable
    get() = if (isLight) Orange else Color.Black

val Colors.taskItemBackgroundColor: Color
    @Composable
    get() = if (isLight) Color.White else DarkGray

val Colors.taskItemTextColor: Color
    @Composable
    get() = if (isLight) DarkGray else LightGray
