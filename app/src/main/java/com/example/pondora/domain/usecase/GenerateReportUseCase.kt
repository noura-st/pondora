package com.example.pondora.domain.usecase

import com.example.pondora.data.remote.OpenAiClient
import com.example.pondora.data.remote.OpenAiMessage
import com.example.pondora.data.remote.OpenAiRequest
import com.example.pondora.data.repository.ScoreRepository
import com.example.pondora.data.repository.UserRepository
import kotlinx.coroutines.flow.first

class GenerateReportUseCase(
    private val scoreRepository: ScoreRepository,
    private val userRepository: UserRepository
) {
    suspend fun generateWeeklyReport(): String {
        val user = userRepository.getUser().first()
        val weeklyScores = scoreRepository.getScoresThisWeek(
            System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L
        ).first()

        if (weeklyScores.isEmpty()) {
            return "Bu hafta henüz görev tamamlamadın. İlk görevini tamamlayınca kişisel raporun burada görünecek!"
        }

        val taskCount = weeklyScores.size
        val avgExpected = weeklyScores.map { it.expectedAnxiety }.average()
        val avgActual = weeklyScores.map { it.actualAnxiety }.average()
        val biasPercent = if (avgExpected > 0) {
            ((avgExpected - avgActual) / avgExpected * 100).toInt()
        } else 0

        val prompt = """
            Sen bir psikoloji asistanısın. Kullanıcının sosyal kaygı verilerini analiz edip 
            kişisel ve motive edici bir haftalık rapor yaz. Türkçe yaz. 
            Bilimsel ama samimi ve sıcak bir dil kullan. 3-4 cümle yaz, çok uzun olmasın.
            
            Kullanıcı verisi:
            - Bu hafta tamamlanan görev sayısı: $taskCount
            - Ortalama beklenen kaygı: ${"%.1f".format(avgExpected)} / 10
            - Ortalama gerçekleşen kaygı: ${"%.1f".format(avgActual)} / 10
            - Zihinsel yanılma oranı: %$biasPercent
            - Mevcut level: ${user?.level ?: 1}
            - Gölet'teki balık sayısı: ${user?.duckCount ?: 0}
            
            Maruz bırakma terapisi bağlamında gelişimi vurgula ve motive et.
        """.trimIndent()

        return try {
            val response = OpenAiClient.service.createResponse(
                OpenAiRequest(
                    messages = listOf(OpenAiMessage(role = "user", content = prompt))
                )
            )

            response.choices
                ?.firstOrNull()
                ?.message
                ?.content
                ?: "Rapor oluşturulamadı, tekrar dene."

        } catch (e: Exception) {
            "Rapor oluşturulurken hata oluştu: ${e.message}"
        }
    }
}