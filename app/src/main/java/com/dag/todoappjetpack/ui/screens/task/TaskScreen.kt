package com.dag.todoappjetpack.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.dag.todoappjetpack.data.model.Priority
import com.dag.todoappjetpack.data.model.TodoTask
import com.dag.todoappjetpack.ui.viewmodels.TodoVM
import com.dag.todoappjetpack.util.Action


@Composable
fun TaskScreen(
    selectedTask: TodoTask?,
    viewModel: TodoVM,
    navigateToListScreen: (Action) -> Unit
){
    val todoTaskTitle:String by viewModel.title
    val todoTaskDescription:String by viewModel.description
    val todoTaskPriority:Priority by viewModel.priority

    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }
     Scaffold(
        topBar = {
            TaskAppBar(
                navigateToListScreen = { action ->
                    navigateToListScreen(action)
                },
                selectedTask = selectedTask
            )
        },
        content = {
            TaskContent(
                title = todoTaskTitle,
                onTitleChange = {
                    viewModel.updateTitle(it)
                },
                description = todoTaskDescription,
                onDescriptionChange = {
                    viewModel.description.value = it
                },
                priority = todoTaskPriority,
                onPriorityChange = {
                    viewModel.priority.value = it
                }
            )
        }
    )
}

fun displayToastMessage(
    context:Context
) {
    Toast.makeText(
        context,
        "Test",
        Toast.LENGTH_LONG
    ).show()
}
 