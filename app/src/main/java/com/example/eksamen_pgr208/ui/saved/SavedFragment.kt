package com.example.eksamen_pgr208.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eksamen_pgr208.R
import com.example.eksamen_pgr208.adapters.SavedAdapter
import com.example.eksamen_pgr208.data.ImageViewModel
import com.example.eksamen_pgr208.databinding.FragmentSavedBinding

class SavedFragment: Fragment() {

    private var binding : FragmentSavedBinding? = null
    private lateinit var imageViewModel: ImageViewModel
    private val bind get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)

        val adapter = SavedAdapter()
        val recyclerView = view.rootView
        recyclerView.adapter = adapter

        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        imageViewModel.readAllData.observe(this, Observer { image ->
            adapter.setData(image)
        })

        binding = FragmentSavedBinding.inflate(inflater, container, false)
        val root : View = bind.root

        val textView : TextView = bind.textSaved
        savedViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}