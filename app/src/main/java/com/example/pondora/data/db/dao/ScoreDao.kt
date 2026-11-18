package com.example.pondora.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pondora.data.db.entity.ScoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Insert
    suspend fun insertScore(score: ScoreEntity)

    @Query("SELECT * FROM scores ORDER BY completedAt DESC")
    fun getAllScores(): Flow<List<ScoreEntity>>

    @Query("SELECT * FROM scores WHERE taskCategory = :category ORDER BY completedAt ASC")
    fun getScoresByCategory(category: String): Flow<List<ScoreEntity>>

    @Query("SELECT AVG(actualAnxiety) FROM scores WHERE taskCategory = :category")
    fun getAverageAnxietyByCategory(category: String): Flow<Float?>

    @Query("SELECT * FROM scores WHERE completedAt >= :startTime")
    fun getScoresThisWeek(startTime: Long): Flow<List<ScoreEntity>>

    @Query("SELECT AVG(expectedAnxiety - actualAnxiety) FROM scores")
    fun getCognitiveBiasAverage(): Flow<Float?>
}