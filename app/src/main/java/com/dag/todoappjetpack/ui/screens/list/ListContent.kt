package com.dag.todoappjetpack.ui.screens.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.dag.todoappjetpack.data.model.Priority
import com.dag.todoappjetpack.data.model.TodoTask
import com.dag.todoappjetpack.ui.theme.*
import com.dag.todoappjetpack.util.RequestState


@Composable
fun ListContent(
    tasks:RequestState<List<TodoTask>>,
    navigateToTaskScreen: (taskId:Int) -> Unit
){
    when(tasks){
        is RequestState.Success ->{
            if (tasks.data.isEmpty()){
                EmptyContent()
            }else{
                DisplayListContent(
                    tasks = tasks.data,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        }
        RequestState.Loading ->{

        }
    }


}

@Composable
fun DisplayListContent(
    tasks:List<TodoTask>,
    navigateToTaskScreen: (taskId:Int) -> Unit
){
    LazyColumn{
        items(
            items = tasks
        ){ task->
            TaskItem(
                todoTask = task,
                navigateToTaskScreen = navigateToTaskScreen
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(
    todoTask: TodoTask,
    navigateToTaskScreen: (taskId:Int) -> Unit
){
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.taskItemBackgroundColor,
        shape = RectangleShape,
        elevation = TASK_ITEM_ELEVATION,
        onClick = {
            navigateToTaskScreen(todoTask.id)
        }
    ) {

        Column(modifier = Modifier
            .padding(LARGE_PADDING)
            .fillMaxWidth()) {
            Row{
                Text(
                    modifier= Modifier.weight(8f),
                    text = todoTask.title,
                    color = MaterialTheme.colors.taskItemTextColor,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd){
                    Canvas(
                        modifier = Modifier
                            .height(PRIORITY_INDICATOR_SIZE)
                            .width(PRIORITY_INDICATOR_SIZE)){
                        drawCircle(
                            color = todoTask.priority.color
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = todoTask.description,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.taskItemTextColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview
fun TaskItemPreview(){
    TaskItem(
        todoTask = TodoTask(0,"Title","Description",Priority.HIGH),
        navigateToTaskScreen = {}
    )
}