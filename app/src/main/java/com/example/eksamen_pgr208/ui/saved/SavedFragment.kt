package com.example.eksamen_pgr208.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.eksamen_pgr208.databinding.FragmentSavedBinding

class SavedFragment: Fragment() {

    private var binding : FragmentSavedBinding? = null

    private val bind get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val savedViewModel = ViewModelProvider(this).get(SavedViewModel::class.java)

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