package com.example.watertracker.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.watertracker.R
import com.example.watertracker.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHistoryBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }
}