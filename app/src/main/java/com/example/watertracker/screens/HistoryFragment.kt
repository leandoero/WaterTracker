package com.example.watertracker.screens

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.watertracker.R
import com.example.watertracker.data.MainDb
import com.example.watertracker.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment(R.layout.fragment_history) {
    lateinit var db: MainDb

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHistoryBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        db = MainDb.getDb(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            val db = MainDb.getDb(requireContext())
            db.getDao().getAllItems().collect { items ->
                val adapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_list_item_1,
                    items)
                binding.listView.adapter = adapter
            }
        }
    }
}