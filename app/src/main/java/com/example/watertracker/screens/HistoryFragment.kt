package com.example.watertracker.screens

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.watertracker.R
import com.example.watertracker.data.Item
import com.example.watertracker.data.MainDb
import com.example.watertracker.databinding.FragmentHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private var currentItems: List<Item> = emptyList()
    lateinit var db: MainDb
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: ArrayAdapter<Item>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHistoryBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        db = MainDb.getDb(requireContext())

        viewLifecycleOwner.lifecycleScope.launch{
            db.getDao().getAllItems().collect { items ->
                currentItems = items
                adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    items
                )
                binding.listView.adapter = adapter
            }
        }

        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val item = currentItems[position]
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                db.getDao().deleteItem(item)  // здесь suspend, работает в IO-потоке
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                lifecycleScope.launch {
                    db.getDao().deleteAllItems()
                    Toast.makeText(requireContext(), "All elements removed", Toast.LENGTH_SHORT).show()
                    binding.checkbox.isChecked = false
                }
            }
        }


    }
}
