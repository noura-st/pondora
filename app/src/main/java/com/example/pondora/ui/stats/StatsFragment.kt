package com.example.pondora.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pondora.databinding.FragmentStatsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChart()
        observeViewModel()
        setupButtons()
    }

    private fun setupChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            axisRight.isEnabled = false
            legend.isEnabled = true
        }
    }

    private fun setupButtons() {
        binding.btnGenerateReport.setOnClickListener {
            binding.btnGenerateReport.isEnabled = false
            binding.btnGenerateReport.text = "Rapor hazırlanıyor..."
            binding.tvAiReport.text = ""
            viewModel.generateAiReport()
        }
    }

    private fun observeViewModel() {
        viewModel.allScores.observe(viewLifecycleOwner) { scores ->
            if (scores.isEmpty()) return@observe

            val actualEntries = scores.mapIndexed { index, score ->
                Entry(index.toFloat(), score.actualAnxiety.toFloat())
            }
            val expectedEntries = scores.mapIndexed { index, score ->
                Entry(index.toFloat(), score.expectedAnxiety.toFloat())
            }

            val actualDataSet = LineDataSet(actualEntries, "Gerçekleşen Kaygı").apply {
                color = Color.parseColor("#4CAF50")
                valueTextColor = Color.BLACK
                lineWidth = 2f
                circleRadius = 4f
                setCircleColor(Color.parseColor("#4CAF50"))
            }

            val expectedDataSet = LineDataSet(expectedEntries, "Beklenen Kaygı").apply {
                color = Color.parseColor("#FF5722")
                valueTextColor = Color.BLACK
                lineWidth = 2f
                circleRadius = 4f
                setCircleColor(Color.parseColor("#FF5722"))
                enableDashedLine(10f, 5f, 0f)
            }

            binding.lineChart.data = LineData(expectedDataSet, actualDataSet)
            binding.lineChart.invalidate()
        }

        viewModel.cognitiveBiasPercent.observe(viewLifecycleOwner) { percent ->
            if (percent > 0) {
                binding.tvCognitiveBias.text = "%$percent"
                binding.tvBiasDescription.text =
                    "Zihnin seni $percent% yanılttı! Beklediğinden daha az korktun."
            }
        }

        viewModel.weeklyScores.observe(viewLifecycleOwner) { scores ->
            if (scores.isEmpty()) {
                binding.tvWeeklySummary.text = "Bu hafta henüz görev tamamlanmadı"
            } else {
                val avgActual = scores.map { it.actualAnxiety }.average()
                val avgExpected = scores.map { it.expectedAnxiety }.average()
                binding.tvWeeklySummary.text =
                    "${scores.size} görev tamamladın!\n" +
                            "Ortalama beklenen kaygı: ${"%.1f".format(avgExpected)}\n" +
                            "Ortalama gerçekleşen kaygı: ${"%.1f".format(avgActual)}"
            }
        }

        viewModel.aiReport.observe(viewLifecycleOwner) { report ->
            binding.tvAiReport.text = report
            binding.btnGenerateReport.isEnabled = true
            binding.btnGenerateReport.text = "Yeni Rapor Oluştur"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}