package com.josejordan.todolistapp.di

import android.content.Context
import androidx.room.Room
import com.josejordan.todolistapp.data.TaskDb
import com.josejordan.todolistapp.data.TaskDao
import com.josejordan.todolistapp.data.TaskRepository
import com.josejordan.todolistapp.data.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDb {
        return Room.databaseBuilder(
            context,
            TaskDb::class.java,
            "task_database"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: TaskDb): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(taskDao)
    }
}
