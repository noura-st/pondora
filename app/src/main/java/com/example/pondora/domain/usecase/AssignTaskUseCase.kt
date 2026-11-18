package com.example.pondora.domain.usecase

import com.example.pondora.data.db.entity.TaskEntity
import com.example.pondora.data.repository.TaskRepository
import com.example.pondora.data.repository.UserRepository
import kotlinx.coroutines.flow.first

class AssignTaskUseCase(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {

    suspend fun getNextDifficulty(): Int {
        val user = userRepository.getUser().first()

        val anxietyLevel = user?.anxietyLevel ?: 2
        val level = user?.level ?: 1

        return when (anxietyLevel) {
            3 -> {
                // Yüksek kaygı: önce çok kolay başlasın
                if (level <= 4) 1 else 2
            }

            2 -> {
                // Orta kaygı: başlangıç kolay, sonra orta
                if (level <= 2) 1 else 2
            }

            else -> {
                // Düşük kaygı: daha cesur görevler
                if (level <= 2) 2 else 3
            }
        }
    }

    suspend fun assignDailyTask(): TaskEntity? {
        val difficulty = getNextDifficulty()

        val suitableTasks = taskRepository
            .getTasksByDifficulty(difficulty)
            .first()

        return suitableTasks.randomOrNull()
    }
}