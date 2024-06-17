package com.example.formulab.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.formulab.R
import com.example.formulab.databinding.FragmentMapBinding

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_map, container, false)

        binding.gameOver.setOnClickListener {
            it.findNavController().navigate(MapFragmentDirections.actionMapFragmentToGameOver())
        }

        binding.tasks.setOnClickListener {
            it.findNavController().navigate(MapFragmentDirections.actionMapFragmentToTasksFragment())
        }

        return binding.root
    }
}