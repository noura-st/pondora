package com.example.pondora.domain.usecase

import com.example.pondora.data.repository.ScoreRepository
import kotlinx.coroutines.flow.first

class AnalyzeProgressUseCase(
    private val scoreRepository: ScoreRepository
) {
    // Bilişsel yanılma: beklenen - gerçekleşen kaygı farkı
    // Pozitif sonuç = zihin aslında olduğundan daha kötü tahmin etti
    suspend fun getCognitiveBiasPercent(): Int {
        val bias = scoreRepository.getCognitiveBiasAverage().first() ?: return 0
        val allScores = scoreRepository.getAllScores().first()
        if (allScores.isEmpty()) return 0
        val avgExpected = allScores.map { it.expectedAnxiety }.average()
        if (avgExpected == 0.0) return 0
        return ((bias / avgExpected) * 100).toInt()
    }

    // Kategori bazlı alışma eğrisi
    // Aynı tip görevler tekrarlandıkça kaygı düşüyor mu?
    suspend fun getHabituationRate(category: String): Float {
        val scores = scoreRepository.getScoresByCategory(category).first()
        if (scores.size < 2) return 0f
        val first = scores.first().actualAnxiety.toFloat()
        val last = scores.last().actualAnxiety.toFloat()
        if (first == 0f) return 0f
        return ((first - last) / first) * 100f
    }

    // Haftalık rapor için bu haftaki puanlar
    suspend fun getWeeklyScores() =
        scoreRepository.getScoresThisWeek(
            System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L
        ).first()
}