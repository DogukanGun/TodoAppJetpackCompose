package com.dag.todoappjetpack.ui.screens.list

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.dag.todoappjetpack.R
import com.dag.todoappjetpack.data.model.TodoTask
import com.dag.todoappjetpack.ui.viewmodels.TodoVM
import com.dag.todoappjetpack.util.RequestState
import com.dag.todoappjetpack.util.SearchBarState
import kotlinx.coroutines.flow.collect


@Composable
fun ListScreen(
    navigateToTaskScreen: (Int) -> Unit,
    viewModel: TodoVM
){
    LaunchedEffect(key1 = true){
        viewModel.getAllTasks()
    }
    val allTasks by viewModel.allTasks.collectAsState()
    val searchAppBarState: SearchBarState by viewModel.searchAppBarState
    val searchTextState: String by viewModel.searchTextState
    Scaffold(
        topBar = {
            ListAppBar(
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState,
                viewModel = viewModel
            )
                 },
        content = {
            ListContent(
                tasks = allTasks,
                navigateToTaskScreen = navigateToTaskScreen
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
