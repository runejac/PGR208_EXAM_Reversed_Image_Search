package com.example.eksamen_pgr208.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.eksamen_pgr208.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private var binding : FragmentSearchBinding? = null
    private val bind get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val searchViewModel = ViewModelProvider(this).get(SearchViewMode::class.java)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root : View = bind.root

        val textView: TextView = bind.textSearch
        searchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}