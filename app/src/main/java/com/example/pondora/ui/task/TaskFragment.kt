package com.example.pondora.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pondora.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSeekBars()
        observeViewModel()
        setupButtons()
        // Sandık animasyonunu oynat
        binding.lottieChest.playAnimation()
        binding.lottieChest.addAnimatorListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
            override fun onAnimationEnd(animation: android.animation.Animator) {
                binding.lottieChest.visibility = android.view.View.GONE
            }
        })
        viewModel.loadDailyTask()
    }

    private fun setupSeekBars() {
        binding.seekBarExpected.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = if (progress == 0) 1 else progress
                binding.tvExpectedValue.text = "$value / 10"
                viewModel.setExpectedAnxiety(value)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBarActual.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = if (progress == 0) 1 else progress
                binding.tvActualValue.text = "$value / 10"
                viewModel.setActualAnxiety(value)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.activeTask.observe(viewLifecycleOwner) { task ->
            if (task != null) {
                binding.tvTaskTitle.text = task.title
                binding.tvTaskDescription.text = task.description
                binding.tvTaskDifficulty.text = when (task.difficulty) {
                    1 -> "Zorluk: Kolay"
                    2 -> "Zorluk: Orta"
                    3 -> "Zorluk: Zor"
                    else -> ""
                }
            } else {
                binding.tvTaskTitle.text = "Bugün görevin yok"
                binding.tvTaskDescription.text = "Yarın yeni bir görev seni bekliyor!"
            }
        }
    }

    private fun setupButtons() {
        binding.btnTaskDone.setOnClickListener {
            binding.layoutActualAnxiety.visibility = View.VISIBLE
            binding.btnTaskDone.visibility = View.GONE
        }

        binding.btnSaveScore.setOnClickListener {
            val task = viewModel.activeTask.value ?: return@setOnClickListener
            viewModel.completeTask(task.id, task.category)

            binding.layoutActualAnxiety.visibility = View.GONE
            binding.btnTaskDone.visibility = View.VISIBLE
            binding.btnTaskDone.isEnabled = true
            binding.btnTaskDone.text = "Görevi Yaptım!"
            binding.seekBarExpected.progress = 5
            binding.seekBarActual.progress = 5
            binding.tvExpectedValue.text = "5 / 10"
            binding.tvActualValue.text = "5 / 10"

            binding.lottieCelebration.visibility = View.VISIBLE
            binding.lottieCelebration.playAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}