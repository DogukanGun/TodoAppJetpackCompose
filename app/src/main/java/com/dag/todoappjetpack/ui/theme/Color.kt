package com.dag.todoappjetpack.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val lowPriority = Color(0xFF00C980)
val mediumPriority = Color(0xFFFFC114)
val highPriority = Color(0xFFFF4646)
val nonePriority = Color(0xFFFFFFFF)

val Colors.taskItemBackgroundColor: Color
    @Composable
    get() = if (isLight) Color.White else Color.Gray

val Colors.taskItemTextColor: Color
    @Composable
    get() = if (isLight) Color.DarkGray else Color.LightGray

val Colors.topAppBarContentColor: Color
    @Composable
    get() = if (isLight) Color.White else Color.LightGray

val Colors.topAppBarColor: Color
    @Composable
    get() = if (isLight) Purple500 else Color.Black