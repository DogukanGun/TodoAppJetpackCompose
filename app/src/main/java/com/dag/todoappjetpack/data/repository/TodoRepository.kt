package com.dag.todoappjetpack.data.repository

import com.dag.todoappjetpack.data.TodoDao
import com.dag.todoappjetpack.data.model.TodoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TodoRepository @Inject constructor(private val todoDao: TodoDao) {
    val getAllTasks: Flow<List<TodoTask>> = todoDao.getAllTasks()
    val sortByLowPriority: Flow<List<TodoTask>> = todoDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<TodoTask>> = todoDao.sortByHighPriority()

    fun getSelectedTask(taskId:Int): Flow<TodoTask>{
        return todoDao.getSelectedTask(taskId)
    }

    suspend fun addTask(todoTask: TodoTask){
        todoDao.addTask(todoTask)
    }

    suspend fun updateTask(todoTask: TodoTask){
        todoDao.updateTask(todoTask)
    }

    suspend fun deleteTask(todoTask: TodoTask){
        todoDao.deleteTask(todoTask)
    }

    suspend fun deleteAll(){
        todoDao.deleteAllTodoList()
    }

    fun searchTodoTask(todoTask: String): Flow<List<TodoTask>>{
        return todoDao.searchTodoTask(todoTask)
    }
}