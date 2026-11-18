package com.example.pondora.domain.usecase

import com.example.pondora.data.repository.UserRepository
import com.example.pondora.util.Constants
import kotlinx.coroutines.flow.first

class GamificationUseCase(
    private val userRepository: UserRepository
) {
    // Görev tamamlandığında XP ekle ve level kontrolü yap
    suspend fun onTaskCompleted() {
        userRepository.addXp(Constants.XP_PER_TASK)
        userRepository.addDuck()
        checkLevelUp()
    }

    // Her 100 XP'de bir level atla
    private suspend fun checkLevelUp() {
        val user = userRepository.getUser().first() ?: return
        val newLevel = (user.xp / 100) + 1
        if (newLevel > user.level) {
            userRepository.updateLevel(newLevel)
        }
    }

    // Level adını döndür
    fun getLevelTitle(level: Int): String {
        return when (level) {
            1 -> "Küçük Balık"
            2 -> "Cesaretlenen Yüzücü"
            3 -> "Akıntıya Karşı"
            4 -> "Derin Su Kâşifi"
            5 -> "Gölet Ustası"
            else -> "Efsanevi Balık"
        }
    }

    // Sonraki level için gereken XP
    fun getXpForNextLevel(currentXp: Int): Int {
        val nextLevelXp = ((currentXp / 100) + 1) * 100
        return nextLevelXp - currentXp
    }
}