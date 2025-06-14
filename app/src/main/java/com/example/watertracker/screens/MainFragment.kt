package com.example.watertracker.screens

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.watertracker.R
import com.example.watertracker.data.Item
import com.example.watertracker.data.MainDb
import com.example.watertracker.databinding.FragmentMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainFragment : Fragment(R.layout.fragment_main) {

    var currentSumVolume: Float = 0f
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val handler = android.os.Handler(Looper.getMainLooper())
    lateinit var db: MainDb
    var goalReached = false
    val dateOnly = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val updateTimeRunnable =
        object : Runnable {        //Runnable - это интерфейс , у которого есть один метод run()
            //Этот метод выполняет то, что я хочу сделать в фоновом или основном потоке.
            //Handler.post(...) и Handler.postDelayed(...) ожидают объект, реализующий интерфейс Runnable.
            override fun run() {
                //Получаем текущее время в формате "HH:mm"
                val currentTime = timeFormat.format(Date())
                binding.localTime.text = currentTime
                //говорит запусти этот Runnable (то есть run()) снова, но через 1 секунду.
                handler.postDelayed(this, 1000) // обновлять каждую секунду
            }
        }
    private fun saveSlider(){
        val slider = binding.slider
        val prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        // Восстановить значение
        val savedValue = prefs.getFloat("slider_value", 0f)
        slider . value = savedValue

        // Сохранять при изменении
        slider.addOnChangeListener { _, value, _ ->
            prefs.edit().putFloat("slider_value", value).apply()
            setColor()
        }
    }

    private fun setColor() {
        val goal = binding.slider.value * 1000
            if(goal <= 0f) {
                // Если цель 0 или меньше — иконка серая
                binding.iconImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ban_main_color))
                goalReached = false
            }
            else if(currentSumVolume >= goal)  {
                // Цель достигнута — иконка серая (можно красной или другой)
                binding.iconImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ban_main_color))
                goalReached = true
            }
            else  {
                // Иначе иконка голубая — цель не достигнута
                binding.iconImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.main_color))
                goalReached = false
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMainBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        db = MainDb.getDb(requireContext())
        /*алгоритм подробно
        * handler.post(updateTimeRunnable) — запускает первое выполнение.
          run():получает текущее время; обновляет TextView; ставит this (себя) на повторное выполнение через 1 секунду.
          Через 1 секунду run() запускается снова — и всё повторяется.
        * */
        handler.post(updateTimeRunnable)
        saveSlider()
        setColor()

        lifecycleScope.launch {
            db.getDao().getSumVolumeByDate(dateOnly).collect { value ->
                currentSumVolume = value ?: 0f
                setColor()
            }
        }

        binding.addButton.setOnClickListener {
            val goal = binding.slider.value * 1000
            if (binding.slider.value <= 0f) {
                Toast.makeText(requireContext(), "Set a goal to continue.", Toast.LENGTH_SHORT).show()
            } else if (currentSumVolume >= goal) {
                binding.iconImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ban_main_color))
                Toast.makeText(requireContext(), "Goal for the day completed!", Toast.LENGTH_SHORT).show()
            } else {
                val selected = binding.spinnerLiters.selectedItem.toString()
                val numberOnly = selected.filter { it.isDigit() || it == '.' }
                val value = numberOnly.toFloat()
                val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                val item = Item(null, value, timeNow, dateOnly)

                lifecycleScope.launch(Dispatchers.IO) {
                    db.getDao().insertItem(item)
                }
            }
        }
    }
}