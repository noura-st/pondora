package com.example.pondora.data.repository

import com.example.pondora.data.db.dao.ScoreDao
import com.example.pondora.data.db.entity.ScoreEntity
import kotlinx.coroutines.flow.Flow

class ScoreRepository(private val scoreDao: ScoreDao) {

    fun getAllScores(): Flow<List<ScoreEntity>> =
        scoreDao.getAllScores()

    fun getScoresByCategory(category: String): Flow<List<ScoreEntity>> =
        scoreDao.getScoresByCategory(category)

    fun getAverageAnxietyByCategory(category: String): Flow<Float?> =
        scoreDao.getAverageAnxietyByCategory(category)

    fun getScoresThisWeek(startTime: Long): Flow<List<ScoreEntity>> =
        scoreDao.getScoresThisWeek(startTime)

    fun getCognitiveBiasAverage(): Flow<Float?> =
        scoreDao.getCognitiveBiasAverage()

    suspend fun insertScore(score: ScoreEntity) =
        scoreDao.insertScore(score)
}