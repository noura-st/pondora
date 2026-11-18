package com.example.pondora.ui.pond

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieAnimationView
import com.example.pondora.R
import com.example.pondora.data.db.AppDatabase
import com.example.pondora.data.repository.UserRepository
import com.example.pondora.domain.usecase.GamificationUseCase

class PondFragment : Fragment() {

    private val viewModel: PondViewModel by viewModels()
    private lateinit var gamificationUseCase: GamificationUseCase

    private lateinit var tvUserName: TextView
    private lateinit var tvLevel: TextView
    private lateinit var tvXp: TextView
    private lateinit var tvLevelTitle: TextView
    private lateinit var progressXp: ProgressBar
    private lateinit var tvXpNext: TextView
    private lateinit var tvDuckCount: TextView
    private lateinit var tvDuckLabel: TextView
    private lateinit var lottieDuck: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pond, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUserName = view.findViewById(R.id.tvUserName)
        tvLevel = view.findViewById(R.id.tvLevel)
        tvXp = view.findViewById(R.id.tvXp)
        tvLevelTitle = view.findViewById(R.id.tvLevelTitle)
        progressXp = view.findViewById(R.id.progressXp)
        tvXpNext = view.findViewById(R.id.tvXpNext)
        tvDuckCount = view.findViewById(R.id.tvDuckCount)
        tvDuckLabel = view.findViewById(R.id.tvDuckLabel)
        lottieDuck = view.findViewById(R.id.lottieDuck)

        val db = AppDatabase.getDatabase(requireContext())
        val userRepository = UserRepository(db.userDao())
        gamificationUseCase = GamificationUseCase(userRepository)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                tvUserName.text = user.name
                tvLevel.text = "Level ${user.level}"
                tvXp.text = "${user.xp} XP"
                tvLevelTitle.text = gamificationUseCase.getLevelTitle(user.level)

                val xpProgress = user.xp % 100
                progressXp.progress = xpProgress

                val xpNext = gamificationUseCase.getXpForNextLevel(user.xp)
                tvXpNext.text = "Sonraki levele $xpNext XP"

                tvDuckCount.text = "🐟 ${user.duckCount}"
                tvDuckLabel.text = if (user.duckCount == 0) {
                    "Henüz balık yok, görev tamamla!"
                } else {
                    "${user.duckCount} balık kazandın!"
                }

                lottieDuck.speed = when {
                    user.duckCount == 0 -> 0.5f
                    user.duckCount < 5 -> 1f
                    user.duckCount < 15 -> 1.5f
                    else -> 2f
                }
            }
        }
    }
}