package com.dag.todoappjetpack.data.model

import androidx.compose.ui.graphics.Color
import com.dag.todoappjetpack.ui.theme.highPriority
import com.dag.todoappjetpack.ui.theme.lowPriority
import com.dag.todoappjetpack.ui.theme.mediumPriority
import com.dag.todoappjetpack.ui.theme.nonePriority

enum class Priority(var color: Color) {
    HIGH(highPriority),
    MEDIUM(mediumPriority),
    LOW(lowPriority),
    NONE(nonePriority)
}