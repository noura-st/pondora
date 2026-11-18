package com.example.pondora.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Int.toAnxietyLabel(): String {
    return when (this) {
        in 1..3 -> "Düşük kaygı"
        in 4..6 -> "Orta kaygı"
        in 7..10 -> "Yüksek kaygı"
        else -> "Bilinmiyor"
    }
}