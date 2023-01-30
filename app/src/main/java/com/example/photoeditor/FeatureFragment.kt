package com.example.photoeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.photoeditor.databinding.FragmentFeatureBinding

class FeatureFragment : Fragment(R.layout.fragment_feature) {

    private lateinit var binding: FragmentFeatureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvCrop.text = arguments?.getString(MainFragment.FEATURE_NAME)
    }
}