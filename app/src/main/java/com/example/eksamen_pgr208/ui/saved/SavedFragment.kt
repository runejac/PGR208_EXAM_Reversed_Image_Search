package com.example.eksamen_pgr208.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eksamen_pgr208.R
import com.example.eksamen_pgr208.adapters.SavedAdapter
import com.example.eksamen_pgr208.data.ImageViewModel
import com.example.eksamen_pgr208.databinding.FragmentSavedBinding
import kotlinx.android.synthetic.main.result_activity.view.*

class SavedFragment: Fragment() {

    private var binding : FragmentSavedBinding? = null
    private lateinit var imageViewModel: ImageViewModel
    private val bind get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.result_activity, container, false)

        val adapter = SavedAdapter(context)
        val recyclerView = view.rv_results
        recyclerView.adapter = adapter

        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        imageViewModel.readAllData.observe(viewLifecycleOwner) { image ->
            println(image)
            adapter.setData(image)
            println(adapter.itemCount)
        }

        binding = FragmentSavedBinding.inflate(inflater, container, false)

        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}