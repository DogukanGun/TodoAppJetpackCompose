package com.dag.todoappjetpack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dag.todoappjetpack.navigation.destinations.listComposable
import com.dag.todoappjetpack.navigation.destinations.splashComposable
import com.dag.todoappjetpack.navigation.destinations.taskComposable
import com.dag.todoappjetpack.ui.viewmodels.TodoVM
import com.dag.todoappjetpack.util.Constant


@Composable
fun SetupNavigation(
    navHostController: NavHostController,
    viewModel:TodoVM
){
    val screen = remember(navHostController) {
        Screens(navHostController = navHostController)
    }
    
    NavHost(
        navController = navHostController,
        startDestination = Constant.SPLASH_SCREEN
    ){
        splashComposable (
            navigateToTaskScreen = screen.splash
        )
        listComposable(
            navigateToTaskScreen = screen.task,
            viewModel = viewModel
        )
        taskComposable(
            navigateToListScreen = screen.list,
            viewModel = viewModel
        )
    }
}