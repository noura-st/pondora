package com.example.pondora.ui.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.pondora.data.db.AppDatabase
import com.example.pondora.data.db.entity.ScoreEntity
import com.example.pondora.data.repository.ScoreRepository
import com.example.pondora.data.repository.UserRepository
import com.example.pondora.domain.usecase.AnalyzeProgressUseCase
import com.example.pondora.domain.usecase.GenerateReportUseCase
import kotlinx.coroutines.launch

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val scoreRepository = ScoreRepository(db.scoreDao())
    private val userRepository = UserRepository(db.userDao())
    private val analyzeProgressUseCase = AnalyzeProgressUseCase(scoreRepository)
    private val generateReportUseCase = GenerateReportUseCase(scoreRepository, userRepository)

    val allScores: LiveData<List<ScoreEntity>> = scoreRepository.getAllScores().asLiveData()

    private val _cognitiveBiasPercent = MutableLiveData<Int>()
    val cognitiveBiasPercent: LiveData<Int> = _cognitiveBiasPercent

    private val _weeklyScores = MutableLiveData<List<ScoreEntity>>()
    val weeklyScores: LiveData<List<ScoreEntity>> = _weeklyScores

    private val _aiReport = MutableLiveData<String>()
    val aiReport: LiveData<String> = _aiReport

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _cognitiveBiasPercent.value = analyzeProgressUseCase.getCognitiveBiasPercent()
            _weeklyScores.value = analyzeProgressUseCase.getWeeklyScores()
        }
    }

    fun generateAiReport() {
        viewModelScope.launch {
            val report = generateReportUseCase.generateWeeklyReport()
            _aiReport.value = report
        }
    }
}