package com.dag.todoappjetpack.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.dag.todoappjetpack.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.dag.todoappjetpack.component.DisplayAlertDialog
import com.dag.todoappjetpack.component.PriorityItem
import com.dag.todoappjetpack.data.model.Priority
import com.dag.todoappjetpack.ui.theme.*
import com.dag.todoappjetpack.ui.viewmodels.TodoVM
import com.dag.todoappjetpack.util.SearchBarState


@Composable
fun ListAppBar(
    viewModel: TodoVM,
    searchAppBarState: SearchBarState,
    searchTextState: String
){
    when(searchAppBarState){
        SearchBarState.CLOSED ->{
            DefaultListAppBar(
                onSearchClick = {
                    viewModel.searchAppBarState.value =
                        SearchBarState.OPENED
                },
                onSortClicked = {
                    viewModel.persistSortingState(it)
                },
                onDeleteAllClicked = {
                    viewModel.deleteAllTask()
                }
            )
        }
        else ->{
            SearchAppBar(
                onTextChange = {
                    viewModel.searchTextState.value = it
                },
                onClosedClick = {
                    viewModel.searchAppBarState.value =
                        SearchBarState.CLOSED
                    viewModel.searchTextState.value = ""
                },
                onSearchClick = {
                    viewModel.searchAllTasks(it)
                },
                text = searchTextState
            )
        }
    }

}

@Composable
fun DefaultListAppBar(
    onSearchClick: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
){
    TopAppBar(
        contentColor = MaterialTheme.colors.topAppBarContentColor,
        title = {
            Text(text = "Tasks")
        },
        actions = {
            ListAppBarActions(
                onSearchClick = onSearchClick,
                onSortClicked = onSortClicked,
                onDeleteAllClicked = onDeleteAllClicked
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarColor
    )
}

@Composable
fun ListAppBarActions(
    onSearchClick: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
){
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = "Delete All Tasks",
        message =  "Are you sure ?",
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        okayButtonClicked = { onDeleteAllClicked() }
    )
    SearchAppBar(onSearchClick = onSearchClick)
    SortAppBar(onSortClicked = onSortClicked)
    DeleteAllActions(onDeleteAllClicked = { openDialog = true })
}

@Composable
fun SearchAppBar(
    onSearchClick: () -> Unit
){
    IconButton(
        onClick = { onSearchClick() }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAllActions(
    onDeleteAllClicked: () -> Unit
){
    var extended by remember { mutableStateOf(false) }
    IconButton(
        onClick = { extended = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_vert),
            contentDescription = "sort",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = extended,
            onDismissRequest = { extended = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    extended = false
                    onDeleteAllClicked()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.delete_tab_bar),
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    style = Typography.subtitle1)
            }
        }
    }
}

@Composable
fun SortAppBar(
    onSortClicked: (Priority) -> Unit
){
    var extended by remember { mutableStateOf(false) }
    IconButton(
        onClick = { extended = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_filter),
            contentDescription = "sort")
        DropdownMenu(
            expanded = extended,
            onDismissRequest = { extended = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    extended = false
                    onSortClicked(Priority.LOW)
                }
            ) {
                PriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(
                onClick = {
                    extended = false
                    onSortClicked(Priority.HIGH)
                }
            ) {
                PriorityItem(priority = Priority.HIGH)
            }
            DropdownMenuItem(
                onClick = {
                    extended = false
                    onSortClicked(Priority.NONE)
                }
            ) {
                PriorityItem(priority = Priority.NONE)
            }
        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onClosedClick: () -> Unit,
    onSearchClick: (String) -> Unit
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarColor) {
            
        TextField(
            value = text,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    text = "Search",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {  },
                    modifier = Modifier.alpha(ContentAlpha.medium)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "search",
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isEmpty()){
                            onClosedClick()
                        }else{
                            onTextChange("")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "",
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick(text)
                }
            )
        )
    }
}

@Composable
@Preview
fun SearchAppBarPreview(){
    SearchAppBar(
        text = "Test",
        onTextChange = {},
        onClosedClick = {  },
        onSearchClick = {}
    )
}

@Composable
@Preview
fun DefaultListAppBarPreview(){
    DefaultListAppBar(
        onSearchClick = {},
        onSortClicked = {},
        onDeleteAllClicked = {}
    )
}