package com.example.pondora.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.pondora.data.db.AppDatabase
import com.example.pondora.data.db.entity.ScoreEntity
import com.example.pondora.data.db.entity.TaskEntity
import com.example.pondora.data.repository.ScoreRepository
import com.example.pondora.data.repository.TaskRepository
import com.example.pondora.data.repository.UserRepository
import com.example.pondora.domain.usecase.AssignTaskUseCase
import com.example.pondora.domain.usecase.GamificationUseCase
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val taskRepository = TaskRepository(db.taskDao())
    private val scoreRepository = ScoreRepository(db.scoreDao())
    private val userRepository = UserRepository(db.userDao())

    private val assignTaskUseCase = AssignTaskUseCase(taskRepository, userRepository)
    private val gamificationUseCase = GamificationUseCase(userRepository)

    val activeTask: LiveData<TaskEntity?> = taskRepository.getActiveTask().asLiveData()

    private val _expectedAnxiety = MutableLiveData(5)
    val expectedAnxiety: LiveData<Int> = _expectedAnxiety

    private val _actualAnxiety = MutableLiveData(5)
    val actualAnxiety: LiveData<Int> = _actualAnxiety

    private val _taskCompleted = MutableLiveData(false)
    val taskCompleted: LiveData<Boolean> = _taskCompleted

    fun setExpectedAnxiety(value: Int) {
        _expectedAnxiety.value = value
    }

    fun setActualAnxiety(value: Int) {
        _actualAnxiety.value = value
    }

    fun completeTask(taskId: Int, category: String) {
        viewModelScope.launch {
            val score = ScoreEntity(
                taskId = taskId,
                taskCategory = category,
                expectedAnxiety = _expectedAnxiety.value ?: 5,
                actualAnxiety = _actualAnxiety.value ?: 5
            )
            scoreRepository.insertScore(score)
            taskRepository.completeTask(taskId)
            gamificationUseCase.onTaskCompleted()
            _taskCompleted.value = true
        }
    }

    fun loadDailyTask() {
        viewModelScope.launch {
            val task = assignTaskUseCase.assignDailyTask()
            task?.let { taskRepository.insertTask(it) }
        }
    }
}