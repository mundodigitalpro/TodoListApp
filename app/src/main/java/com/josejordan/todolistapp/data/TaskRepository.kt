package com.josejordan.todolistapp.data

import androidx.lifecycle.LiveData

interface TaskRepository {

    val allTasks: LiveData<List<Task>>

    suspend fun insert(task: Task)

    suspend fun update(task: Task)

    suspend fun delete(task: Task)
}

