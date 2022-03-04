package com.dag.todoappjetpack.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dag.todoappjetpack.data.model.Priority
import com.dag.todoappjetpack.data.model.TodoTask
import com.dag.todoappjetpack.data.repository.DataStoreRepository
import com.dag.todoappjetpack.data.repository.TodoRepository
import com.dag.todoappjetpack.ui.theme.MAX_TITLE_LENGTH
import com.dag.todoappjetpack.util.Action
import com.dag.todoappjetpack.util.RequestState
import com.dag.todoappjetpack.util.SearchBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class TodoVM @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val repository: TodoRepository
): ViewModel() {

    val action:MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    private val emptyTodoTask = TodoTask(0,"","",Priority.HIGH)
    val id:MutableState<Int> = mutableStateOf(-1)
    val title:MutableState<String> = mutableStateOf("")
    val description:MutableState<String> = mutableStateOf("")
    val priority:MutableState<Priority> = mutableStateOf(Priority.LOW)

    private val _allTasks = MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Loading)
    val allTasks:StateFlow<RequestState<List<TodoTask>>> = _allTasks


    val lowPriorityTasks:StateFlow<List<TodoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )
    val highPriorityTasks:StateFlow<List<TodoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )
    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Loading)
    val sortState:StateFlow<RequestState<Priority>> = _sortState

    private val _selectedTask: MutableStateFlow<TodoTask?> = MutableStateFlow(null)
    var selectedTask: StateFlow<TodoTask?> = _selectedTask

    val searchTextState: MutableState<String> = mutableStateOf("")
    val searchAppBarState: MutableState<SearchBarState> = mutableStateOf(SearchBarState.CLOSED)
    private val _searchTasks = MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Loading)
    val searchTasks = _searchTasks


    fun getAllTasks(){
        _allTasks.value = RequestState.Loading
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

    fun searchAllTasks(search:String){
        _searchTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchTodoTask(todoTask = "%$search%")
                    .collect {  searchList ->
                        searchTasks.value = RequestState.Success(searchList)
                    }
            }
        }catch (error:Exception){
            _allTasks.value = RequestState.Error(error)
        }
        searchAppBarState.value = SearchBarState.TRIGGERED
    }

    fun deleteAllTask(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun addTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = TodoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.addTask(todoTask)
        }
        searchAppBarState.value = SearchBarState.CLOSED
    }

    fun updateTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = TodoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.updateTask(todoTask)
        }
    }

    fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = TodoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.deleteTask(todoTask)
        }
    }

    fun handleAction(action: Action){
        when(action){
            Action.ADD ->{
                addTask()
            }
            Action.UPDATE ->{
                updateTask()
            }
            Action.DELETE ->{
                deleteTask()
            }
            Action.UNDO ->{
                addTask()
            }
            Action.DELETE_ALL ->{
                deleteAllTask()
            }
            else ->{
            }
        }
        this.action.value = Action.NO_ACTION
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

    fun updateTaskTask(selectedTask:TodoTask?){
        if (selectedTask != null){
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        }else{
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun updateTitle(newTitle : String){
        if (newTitle.length < MAX_TITLE_LENGTH){
            title.value = newTitle
        }
    }

    fun persistSortingState(priority: Priority){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }

    fun readSortState(){
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readState
                    .map { Priority.valueOf(it) }
                    .collect { _sortState.value = RequestState.Success(it) }
            }
        }catch (error:Exception){
            _sortState.value = RequestState.Error(error)
        }
    }
}