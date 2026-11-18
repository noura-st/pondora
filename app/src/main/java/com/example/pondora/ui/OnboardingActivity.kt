package com.example.pondora.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pondora.MainActivity
import com.example.pondora.data.db.AppDatabase
import com.example.pondora.data.db.entity.UserEntity
import com.example.pondora.data.repository.UserRepository
import com.example.pondora.databinding.ActivityOnboardingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private var currentQuestion = 0
    private val answers = mutableListOf<Int>()

    private val questions = listOf(
        "Tanımadığın biriyle göz teması kurmak seni ne kadar zorluyor?",
        "Kalabalık bir ortamda konuşmak zorunda kaldığında ne hissediyorsun?",
        "Birinden bir şey istemek seni ne kadar kaygılandırıyor?",
        "Sosyal ortamlarda hata yapma korkusu ne kadar yoğun?",
        "Yeni insanlarla tanışmak seni ne kadar geriyor?"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showQuestion()

        binding.btnNext.setOnClickListener {
            val answer = binding.seekBarQuestion.progress
            answers.add(if (answer == 0) 1 else answer)

            if (currentQuestion < questions.size - 1) {
                currentQuestion++
                showLoadingThenQuestion()
            } else {
                saveResultAndContinue()
            }
        }
    }

    private fun showLoadingThenQuestion() {
        binding.lottieLoading.visibility = android.view.View.VISIBLE
        binding.lottieLoading.playAnimation()
        binding.tvAnalyzing.visibility = android.view.View.VISIBLE
        binding.btnNext.isEnabled = false

        lifecycleScope.launch {
            delay(1200)
            binding.lottieLoading.visibility = android.view.View.GONE
            binding.tvAnalyzing.visibility = android.view.View.GONE
            binding.btnNext.isEnabled = true
            showQuestion()
        }
    }

    private fun showQuestion() {
        binding.tvQuestionNumber.text = "Soru ${currentQuestion + 1} / ${questions.size}"
        binding.tvQuestion.text = questions[currentQuestion]
        binding.seekBarQuestion.progress = 5
        binding.tvAnswerValue.text = "5 / 10"
        binding.btnNext.text = if (currentQuestion == questions.size - 1) "Bitir" else "Sonraki"

        binding.seekBarQuestion.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: android.widget.SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val value = if (progress == 0) 1 else progress
                binding.tvAnswerValue.text = "$value / 10"
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
    }

    private fun saveResultAndContinue() {
        val avgScore = answers.average()
        val anxietyLevel = when {
            avgScore <= 3 -> 1
            avgScore <= 6 -> 2
            else -> 3
        }

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val userRepository = UserRepository(db.userDao())
            val existingUser = userRepository.getUser().first()
            if (existingUser != null) {
                userRepository.updateUser(existingUser.copy(anxietyLevel = anxietyLevel))
            } else {
                userRepository.insertUser(UserEntity(anxietyLevel = anxietyLevel))
            }
            val resultMessage = when (anxietyLevel) {
                1 -> "Harika! Hafif kaygı seviyesin var. Sana orta zorlukta görevlerle başlayacağız."
                2 -> "Anlıyoruz. Orta düzey kaygın var. Küçük adımlarla başlayıp ilerleyeceğiz."
                else -> "Cesur bir adım attın! Yüksek kaygın olsa da birlikte üstesinden geleceğiz."
            }

            android.app.AlertDialog.Builder(this@OnboardingActivity)
                .setTitle("Sonucun Hazır! 🐟")
                .setMessage(resultMessage)
                .setPositiveButton("Hadi Başlayalım!") { _, _ ->
                    startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
                    finish()
                }
                .show()
        }
    }
}