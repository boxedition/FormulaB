package com.example.formulab.fragments

import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.formulab.R
import com.example.formulab.databinding.FragmentStartBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_start,container,false)

        binding.playButton.setOnClickListener {
            it.findNavController().navigate(StartFragmentDirections.actionStartFragmentToMapFragment())
        }

        binding.cameraButton.setOnClickListener{
            it.findNavController().navigate(StartFragmentDirections.actionStartFragmentToCameraFragment())
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }
}