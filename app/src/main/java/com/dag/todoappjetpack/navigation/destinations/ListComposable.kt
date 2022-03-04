package com.dag.todoappjetpack.navigation.destinations

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dag.todoappjetpack.ui.screens.list.ListScreen
import com.dag.todoappjetpack.ui.screens.splash.SplashScreen
import com.dag.todoappjetpack.ui.screens.task.TaskScreen
import com.dag.todoappjetpack.ui.viewmodels.TodoVM
import com.dag.todoappjetpack.util.Action
import com.dag.todoappjetpack.util.Constant
import com.dag.todoappjetpack.util.toAction


fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    viewModel: TodoVM
){
    composable(
        route = Constant.LIST_SCREEN,
        arguments = listOf(navArgument(Constant.LIST_SCREEN_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){ navBackStackEntry ->
        val action = navBackStackEntry.arguments?.getString(Constant.LIST_SCREEN_ARGUMENT_KEY).toAction()
        var myAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

        LaunchedEffect(key1 = myAction){
            if (action != myAction){
                myAction = action
                viewModel.action.value = action
            }
        }
        val databaseAction by viewModel.action

        ListScreen(
            action = databaseAction,
            navigateToTaskScreen = navigateToTaskScreen,
            viewModel = viewModel
        )
    }

}

fun NavGraphBuilder.splashComposable(
    navigateToTaskScreen: () -> Unit
){
    composable(
        route = Constant.SPLASH_SCREEN,
    ){ navBackStackEntry ->
        SplashScreen(
            navigateToTaskScreen = navigateToTaskScreen
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
        LaunchedEffect(key1 = taskId){
            viewModel.getSelectedTask(taskId)
        }
        val selectedTask by viewModel.selectedTask.collectAsState()
        LaunchedEffect(key1 = selectedTask){
            if (selectedTask != null || taskId == -1){
                viewModel.updateTaskTask(selectedTask)
            }
        }

        TaskScreen(
            selectedTask = selectedTask,
            viewModel = viewModel,
            navigateToListScreen = navigateToListScreen
        )
    }

}