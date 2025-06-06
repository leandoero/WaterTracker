package com.example.watertracker.screens

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.watertracker.R
import com.example.watertracker.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.logging.Handler

class MainFragment : Fragment(R.layout.fragment_main) {

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val currentTime = timeFormat.format(Date())
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMainBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        binding.localTime.text = currentTime
        binding.addButton.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "ASDADA", Toast.LENGTH_SHORT)
            toast.show()
        }

    }
}