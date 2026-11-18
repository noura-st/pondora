package com.example.pondora.data.repository

import com.example.pondora.data.db.dao.TaskDao
import com.example.pondora.data.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getActiveTask(): Flow<TaskEntity?> =
        taskDao.getActiveTask()

    fun getCompletedTasks(): Flow<List<TaskEntity>> =
        taskDao.getCompletedTasks()

    fun getTasksByDifficulty(difficulty: Int): Flow<List<TaskEntity>> =
        taskDao.getTasksByDifficulty(difficulty)

    suspend fun insertTask(task: TaskEntity) =
        taskDao.insertTask(task)

    suspend fun completeTask(taskId: Int) =
        taskDao.completeTask(taskId)

    suspend fun updateTask(task: TaskEntity) =
        taskDao.updateTask(task)
    suspend fun getTaskCount(): Int = taskDao.getTaskCount()
}