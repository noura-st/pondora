package com.example.pondora.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,           // "Kantindeki görevliye kolay gelsin de"
    val description: String,     // Görevin açıklaması
    val difficulty: Int,         // 1=kolay, 2=orta, 3=zor
    val category: String,        // "selam_verme", "soru_sorma" vb.
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)