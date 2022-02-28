package com.dag.todoappjetpack.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dag.todoappjetpack.util.Constant

@Entity(tableName = Constant.DATABASE_TABLE)
data class TodoTask (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val title:String,
    val description:String,
    val priority: Priority
)