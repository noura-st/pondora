package com.example.pondora.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pondora.data.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY createdAt DESC LIMIT 1")
    fun getActiveTask(): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE difficulty = :difficulty AND isCompleted = 0")
    fun getTasksByDifficulty(difficulty: Int): Flow<List<TaskEntity>>

    @Query("UPDATE tasks SET isCompleted = 1 WHERE id = :taskId")
    suspend fun completeTask(taskId: Int)
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTaskCount(): Int
}