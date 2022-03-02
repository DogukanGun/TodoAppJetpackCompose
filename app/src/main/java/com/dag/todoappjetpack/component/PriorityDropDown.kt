package com.dag.todoappjetpack.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dag.todoappjetpack.data.model.Priority
import com.dag.todoappjetpack.ui.theme.PRIORITY_DROPDOWN_SIZE
import com.dag.todoappjetpack.ui.theme.PRIORITY_INDICATOR_SIZE
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PriorityDropDown(
    priority: Priority,
    selectedPriority: (Priority) -> Unit
){
    var expended by remember { mutableStateOf(false) }
    val angle:Float by animateFloatAsState(
        targetValue = if (expended) 180f else 0f
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(PRIORITY_DROPDOWN_SIZE)
            .background(MaterialTheme.colors.background)
            .clickable { expended = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
                .weight(1f)
        ){
            drawCircle(color = priority.color)
        }
        Text(
            modifier = Modifier
                .weight(7f),
            text = priority.name,
            style = MaterialTheme.typography.subtitle2
        )
        IconButton(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .weight(1f)
                .rotate(angle),
            onClick = { expended = true }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "drop-down"
            )
        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expended,
            onDismissRequest = { expended = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expended = false
                    selectedPriority(Priority.LOW)
                }
            ) {
                PriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(
                onClick = {
                    expended = false
                    selectedPriority(Priority.MEDIUM)
                }
            ) {
                PriorityItem(priority = Priority.MEDIUM)
            }
            DropdownMenuItem(
                onClick = {
                    expended = false
                    selectedPriority(Priority.HIGH)
                }
            ) {
                PriorityItem(priority = Priority.HIGH)
            }
        }
    }
}

@Composable
@Preview
fun PriorityDropDownPreview(){
    PriorityDropDown(
        priority = Priority.MEDIUM,
        selectedPriority = {}
    )
}