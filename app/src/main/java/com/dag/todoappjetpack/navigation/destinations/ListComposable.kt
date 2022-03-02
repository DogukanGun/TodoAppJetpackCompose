package com.dag.todoappjetpack.navigation.destinations

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dag.todoappjetpack.ui.screens.list.ListScreen
import com.dag.todoappjetpack.ui.screens.task.TaskScreen
import com.dag.todoappjetpack.ui.viewmodels.TodoVM
import com.dag.todoappjetpack.util.Action
import com.dag.todoappjetpack.util.Constant


fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    viewModel: TodoVM
){
    composable(
        route = Constant.LIST_SCREEN,
        arguments = listOf(navArgument(Constant.LIST_SCREEN_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){
        ListScreen(
            navigateToTaskScreen = navigateToTaskScreen,
            viewModel = viewModel
        )
    }

}

fun NavGraphBuilder.taskComposable(
    navigateToListScreen: (Action) -> Unit,
    viewModel: TodoVM
){
    composable(
        route = Constant.TASK_SCREEN,
        arguments = listOf(navArgument(Constant.TASK_SCREEN_ARGUMENT_KEY){
            type = NavType.IntType
        })
    ){ navBackStackEntry ->
        val taskId = navBackStackEntry.arguments!!.getInt(Constant.TASK_SCREEN_ARGUMENT_KEY)
        viewModel.getSelectedTask(taskId)
        val selectedTask by viewModel.selectedTask.collectAsState()
        TaskScreen(
            selectedTask = selectedTask,
            navigateToListScreen = navigateToListScreen
        )
    }

}