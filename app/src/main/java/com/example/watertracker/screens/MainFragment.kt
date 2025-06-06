package com.example.watertracker.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.watertracker.R
import com.example.watertracker.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMainBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }
}