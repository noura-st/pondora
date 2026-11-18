package com.example.pondora

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pondora.data.db.AppDatabase
import com.example.pondora.data.db.TaskSeeder
import com.example.pondora.data.repository.TaskRepository
import com.example.pondora.data.repository.UserRepository
import com.example.pondora.databinding.ActivityMainBinding
import com.example.pondora.util.NotificationScheduler
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        // Bildirim izni iste (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1
            )
        }

        // Günlük bildirimi planla
        NotificationScheduler.scheduleDailyReminder(this)

        // Veritabanını başlat
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val taskRepository = TaskRepository(db.taskDao())
            val userRepository = UserRepository(db.userDao())
            TaskSeeder.seedIfEmpty(taskRepository, userRepository)
        }
    }
}