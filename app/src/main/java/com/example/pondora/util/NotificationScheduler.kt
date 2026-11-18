package com.example.pondora.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun scheduleDailyReminder(context: Context) {
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (currentTime.after(targetTime)) {
            targetTime.add(Calendar.DAY_OF_MONTH, 1)
        }

        val delay = targetTime.timeInMillis - currentTime.timeInMillis

        val dailyRequest = PeriodicWorkRequestBuilder<TaskReminderWorker>(
            24, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_task_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyRequest
        )

        // Haftalık rapor - her 7 günde bir
        val weeklyRequest = PeriodicWorkRequestBuilder<TaskReminderWorker>(
            7, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "weekly_report_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            weeklyRequest
        )
    }
}