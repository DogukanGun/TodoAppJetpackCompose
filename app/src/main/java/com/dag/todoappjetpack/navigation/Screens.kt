package com.dag.todoappjetpack.navigation

import androidx.navigation.NavHostController
import com.dag.todoappjetpack.util.Action
import com.dag.todoappjetpack.util.Constant

class Screens(navHostController: NavHostController) {
    val list:(Action) -> Unit = { action->
        navHostController.navigate("list/${action.name}"){
            popUpTo(Constant.LIST_SCREEN){inclusive = true}
        }
    }

    val task:(Int) -> Unit = { taskId->
        navHostController.navigate("task/$taskId")
    }
}