package com.dag.todoappjetpack.ui.screens.task

import android.app.Notification
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.dag.todoappjetpack.component.DisplayAlertDialog
import com.dag.todoappjetpack.data.model.Priority
import com.dag.todoappjetpack.data.model.TodoTask
import com.dag.todoappjetpack.ui.theme.topAppBarColor
import com.dag.todoappjetpack.ui.theme.topAppBarContentColor
import com.dag.todoappjetpack.util.Action


@Composable
fun TaskAppBar(
    selectedTask: TodoTask?,
    navigateToListScreen: (Action) -> Unit
){
    if (selectedTask == null){
        NewTaskAppBar(
            navigateToListScreen =  navigateToListScreen
        )
    }else{
        ExistingTaskAppBar(
            selectedTask = selectedTask,
            navigateToListScreen = navigateToListScreen)
    }

}

@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action) -> Unit
){
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = "Task",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarColor,
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }
    )
}


@Composable
fun ExistingTaskAppBar(
    selectedTask: TodoTask,
    navigateToListScreen: (Action) -> Unit
){
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = selectedTask.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarColor,
        actions = {
            ExistingTaskAppBarActions(
                selectedTask = selectedTask,
                navigateToListScreen = navigateToListScreen
            )
        }
    )
}

@Composable
fun ExistingTaskAppBarActions(
    selectedTask: TodoTask,
    navigateToListScreen: (Action) -> Unit
){
    var openDialog by remember { mutableStateOf(false) }
    DisplayAlertDialog(
        title = "Delete Task",
        message = "Are You Sure ?",
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        okayButtonClicked = { navigateToListScreen(Action.DELETE)}
    )
    DeleteAction(onDeleteClicked = {
        openDialog = true
    })
    UpdateAction(onUpdateClicked = navigateToListScreen)
}
@Composable
fun CloseAction(
    onCloseClicked : (Action) -> Unit
){
    IconButton(
        onClick = { onCloseClicked(Action.NO_ACTION) }
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked : (Action) -> Unit
){
    IconButton(
        onClick = { onDeleteClicked(Action.DELETE) }
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Close",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked : (Action) -> Unit
){
    IconButton(
        onClick = { onUpdateClicked(Action.UPDATE) }
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Close",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}


@Composable
fun BackAction(
    onBackClicked : (Action) -> Unit
){
    IconButton(
        onClick = { onBackClicked(Action.NO_ACTION) }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun AddAction(
    onAddClicked : (Action) -> Unit
){
    IconButton(
        onClick = { onAddClicked(Action.ADD) }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Back",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
@Preview
fun NewTaskAppBarPreview(){
    NewTaskAppBar(
        navigateToListScreen = {}
    )
}

@Composable
@Preview
fun ExistingTaskAppBarPreview(){
    ExistingTaskAppBar(
        selectedTask = TodoTask(0,"Task","Desc",Priority.HIGH),
        navigateToListScreen = {}
    )
}