package com.example.pondora.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: Int = 1,              // Tek kullanıcı olacak, id hep 1
    val name: String = "Kahraman",
    val anxietyLevel: Int = 3,    // Başlangıç kaygı seviyesi 1=az, 2=orta, 3=çok
    val xp: Int = 0,              // Toplam tecrübe puanı
    val level: Int = 1,           // Mevcut seviye
    val duckCount: Int = 0,       // Göleттeki ördek sayısı
    val createdAt: Long = System.currentTimeMillis()
)