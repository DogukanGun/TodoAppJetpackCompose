package com.dag.todoappjetpack.data

import androidx.room.*
import com.dag.todoappjetpack.data.model.TodoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table ORDER BY id DESC ")
    fun getAllTasks(): Flow<List<TodoTask>>

    @Query("SELECT * FROM TODO_TABLE WHERE id=:taskId")
    fun getSelectedTask(taskId:Int): Flow<TodoTask>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(todoTask: TodoTask)

    @Update
    suspend fun updateTask(todoTask: TodoTask)

    @Delete
    suspend fun deleteTask(todoTask: TodoTask)

    @Query("DELETE FROM TODO_TABLE")
    suspend fun deleteAllTodoList()

    @Query("SELECT * FROM todo_table WHERE title LIKE :search OR description LIKE :search")
    fun searchTodoTask(search:String): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): Flow<List<TodoTask>>
}