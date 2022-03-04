package com.dag.todoappjetpack.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.dag.todoappjetpack.data.model.Priority
import com.dag.todoappjetpack.data.model.TodoTask
import com.dag.todoappjetpack.ui.theme.*
import com.dag.todoappjetpack.util.Action
import com.dag.todoappjetpack.util.RequestState
import com.dag.todoappjetpack.util.SearchBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ListContent(
    allTasks:RequestState<List<TodoTask>>,
    lowPriorityTaskList:List<TodoTask>,
    highPriorityTaskList:List<TodoTask>,
    onSwipeToDelete:(Action, TodoTask) -> Unit,
    priorityState: RequestState<Priority>,
    searchTask:RequestState<List<TodoTask>>,
    searchAppBarState: SearchBarState,
    navigateToTaskScreen: (taskId:Int) -> Unit
){
    if (priorityState is RequestState.Success){
        when{
            searchAppBarState == SearchBarState.TRIGGERED ->{
                if (searchTask is RequestState.Success){
                    HandleListContent(
                        task = searchTask.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
            priorityState.data == Priority.NONE ->{
                if (allTasks is RequestState.Success){
                    HandleListContent(
                        task = allTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
            priorityState.data == Priority.LOW ->{
                HandleListContent(
                    task = lowPriorityTaskList,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }
            priorityState.data == Priority.HIGH ->{
                HandleListContent(
                    task = highPriorityTaskList,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }
        }
    }
}

@Composable
fun HandleListContent(
    task:List<TodoTask>,
    onSwipeToDelete:(Action, TodoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
){
    if (task.isEmpty()){
        EmptyContent()
    }else{
        DisplayListContent(
            tasks = task,
            onSwipeToDelete = onSwipeToDelete,
            navigateToTaskScreen = navigateToTaskScreen
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayListContent(
    tasks:List<TodoTask>,
    onSwipeToDelete:(Action, TodoTask) -> Unit,
    navigateToTaskScreen: (taskId:Int) -> Unit
){
    LazyColumn{
        items(
            items = tasks,
            key = { task ->
                task.id
            }
        ){ task->
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismiss = dismissState.isDismissed(DismissDirection.EndToStart)
            if (isDismiss && dismissDirection == DismissDirection.EndToStart){
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(300)
                    onSwipeToDelete(Action.DELETE,task)
                }
            }
            val degrees by animateFloatAsState(targetValue =
                if (dismissState.targetValue == DismissValue.Default)
                    0f
                else
                    -45f
            )
            var itemAppear by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = true ){
                itemAppear = true
            }
            AnimatedVisibility(
                visible = itemAppear && !isDismiss,
                enter = expandVertically (
                    animationSpec = tween(
                        durationMillis = 200
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 200
                    )
                )
            ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.3f) },
                    background = { RedBackground(degrees = degrees) },
                    dismissContent = {
                        TaskItem(
                            todoTask = task,
                            navigateToTaskScreen = navigateToTaskScreen
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun RedBackground(degrees:Float){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(highPriority)
            .padding(MEDIUM_PADDING),
        contentAlignment = Alignment.CenterEnd,
    ){
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete Icon",
            tint = Color.White
        )
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