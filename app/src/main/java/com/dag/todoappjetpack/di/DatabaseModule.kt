package com.dag.todoappjetpack.di

import android.content.Context
import androidx.room.Room
import com.dag.todoappjetpack.data.TodoDatabase
import com.dag.todoappjetpack.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context:Context
    ) = Room.databaseBuilder(context, TodoDatabase::class.java,Constant.DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDao(todoDatabase: TodoDatabase) = todoDatabase.todoDao()
}