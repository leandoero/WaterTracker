package com.example.watertracker.screens

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.example.watertracker.R
import com.example.watertracker.data.Item
import com.example.watertracker.data.MainDb
import com.example.watertracker.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.logging.Handler

class MainFragment : Fragment(R.layout.fragment_main) {

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val handler = android.os.Handler(Looper.getMainLooper())
    lateinit var db: MainDb

    private val updateTimeRunnable = object : Runnable {        //Runnable - это интерфейс , у которого есть один метод run()
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

        binding.addButton.setOnClickListener {
            if(binding.slider.value > 0f){
                val selected = binding.spinnerLiters.selectedItem.toString()
                val numberOnly = selected.filter { it.isDigit() || it == '.' }
                val value = numberOnly.toFloat()
                val time = binding.localTime.text.toString()
                val dateOnly = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


                val item = Item(null,value, time, dateOnly)
                Thread{
                    db.getDao().insertItem(item)
                }.start()
                val toast = Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                val toast = Toast.makeText(requireContext(), "Set a goal to continue.", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }
}