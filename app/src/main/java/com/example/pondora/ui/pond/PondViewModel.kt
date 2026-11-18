package com.example.pondora.ui.pond

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.pondora.data.db.AppDatabase
import com.example.pondora.data.db.entity.UserEntity
import com.example.pondora.data.repository.UserRepository

class PondViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val userRepository = UserRepository(db.userDao())

    val user: LiveData<UserEntity?> = userRepository.getUser().asLiveData()
}