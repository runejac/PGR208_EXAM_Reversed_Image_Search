package com.example.eksamen_pgr208.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.example.eksamen_pgr208.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var binding : FragmentHomeBinding? = null

    private val bind get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        val root: View = bind.root

        val textView : TextView = bind.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val subTextView : TextView = bind.subtitleHome
        homeViewModel.subText.observe(viewLifecycleOwner) {
            subTextView.text = it
        }



        return root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}