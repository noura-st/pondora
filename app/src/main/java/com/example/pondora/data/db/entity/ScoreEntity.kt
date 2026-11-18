package com.example.pondora.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int,              // Hangi göreve ait
    val taskCategory: String,     // Grafik için kategori bazlı analiz
    val expectedAnxiety: Int,     // Beklenen kaygı (1-10) göreve başlamadan önce
    val actualAnxiety: Int,       // Gerçekleşen kaygı (1-10) görev bittikten sonra
    val completedAt: Long = System.currentTimeMillis()
)