package com.dag.todoappjetpack.ui.screens.list

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.dag.todoappjetpack.R
import com.dag.todoappjetpack.data.model.TodoTask
import com.dag.todoappjetpack.ui.viewmodels.TodoVM
import com.dag.todoappjetpack.util.Action
import com.dag.todoappjetpack.util.RequestState
import com.dag.todoappjetpack.util.SearchBarState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (Int) -> Unit,
    viewModel: TodoVM
){
    LaunchedEffect(key1 = true){
        viewModel.getAllTasks()
        viewModel.readSortState()
    }
    LaunchedEffect(key1 = action){
        viewModel.handleAction(action = action)
    }
    val sortState by viewModel.sortState.collectAsState()
    val lowPriorityTaskList by viewModel.lowPriorityTasks.collectAsState()
    val highPriorityTaskList by viewModel.highPriorityTasks.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val searchTasks by viewModel.searchTasks.collectAsState()
    val searchAppBarState: SearchBarState by viewModel.searchAppBarState
    val searchTextState: String by viewModel.searchTextState
    val scaffoldState = rememberScaffoldState()

    DisplaySnackbar(
        scaffoldState = scaffoldState,
        onComplete = { viewModel.action.value = it },
        taskTitle = viewModel.title.value,
        action = action,
        onUndoClicked = {
            viewModel.action.value = it
        }
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState,
                viewModel = viewModel
            )
                 },
        content = {
            ListContent(
                allTasks = allTasks,
                searchTask = searchTasks,
                searchAppBarState = searchAppBarState,
                navigateToTaskScreen = navigateToTaskScreen,
                lowPriorityTaskList = lowPriorityTaskList,
                highPriorityTaskList = highPriorityTaskList,
                priorityState = sortState,
                onSwipeToDelete = { action,task ->
                    viewModel.action.value = action
                    viewModel.updateTaskTask(task)
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }
            )
        },
        floatingActionButton = {
            ListFabButton(navigateToTaskScreen = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFabButton(
    navigateToTaskScreen: (Int) -> Unit
){
    FloatingActionButton(onClick = {
        navigateToTaskScreen(-1)
    }) {
        Icon(imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.fab_button_text),
            tint = Color.White)
    }
}

@Composable
fun DisplaySnackbar(
    scaffoldState:ScaffoldState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
){
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action){
        if (action != Action.NO_ACTION){
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action = action, taskTitle = taskTitle),
                    actionLabel = setActionLabel(action = action)
                )
                UndoDeletedTask(
                    action = action,
                    snackbarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    }

}

private fun setMessage(action: Action,taskTitle: String):String{
    return when(action){
        Action.ADD ->{
            "$taskTitle is added"
        }
        Action.UPDATE ->{
            "$taskTitle is updated"
        }
        Action.DELETE ->{
            "$taskTitle is deleted"
        }
        Action.UNDO ->{
            "$taskTitle is added again"
        }
        Action.DELETE_ALL ->{
            "All tasks are deleted"
        }
        else ->{
            ""
        }
    }
}

private fun setActionLabel(action: Action) : String{
    return if (action == Action.DELETE){
        "UNDO"
    }else{
        "OK"
    }
}

private fun UndoDeletedTask(
    action: Action,
    snackbarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
){
    if (snackbarResult == SnackbarResult.ActionPerformed
        && action == Action.DELETE){
        onUndoClicked(Action.UNDO)
    }
}