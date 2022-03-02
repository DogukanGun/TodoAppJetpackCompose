package com.dag.todoappjetpack.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dag.todoappjetpack.data.model.TodoTask
import com.dag.todoappjetpack.data.repository.TodoRepository
import com.dag.todoappjetpack.util.RequestState
import com.dag.todoappjetpack.util.SearchBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class TodoVM @Inject constructor(
    private val repository: TodoRepository
): ViewModel() {

    private val _allTasks = MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Loading)
    val searchAppBarState: MutableState<SearchBarState> = mutableStateOf(SearchBarState.CLOSED)

    private val _selectedTask: MutableStateFlow<TodoTask?> = MutableStateFlow(null)
    var selectedTask: StateFlow<TodoTask?> = _selectedTask

    val searchTextState: MutableState<String> = mutableStateOf("")

    val allTasks = _allTasks

    fun getAllTasks(){
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        }catch (error:Exception){
            _allTasks.value = RequestState.Error(error)
        }
    }

    fun getSelectedTask(taskId:Int){
        try {
            viewModelScope.launch {
                repository.getSelectedTask(taskId).collect {
                    _selectedTask.value = it
                }
            }
        }catch (error:Exception){

        }
    }
}