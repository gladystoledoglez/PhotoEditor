package com.example.photoeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.photoeditor.databinding.FragmentColorBinding
import com.example.photoeditor.extensions.getBitmap
import com.example.photoeditor.extensions.setOnSeekBarProgressChanged

class ColorFragment : Fragment() {
    private lateinit var binding: FragmentColorBinding
    private val viewModel: ColorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentColorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.changeImage(arguments?.getBitmap(MainFragment.colorImage))

        with(binding) {
            tvSaturation.text = getString(R.string.tv_saturation, 0)
            sbSaturation.setOnSeekBarProgressChanged { _, progress: Int, _ ->
                tvSaturation.text = getString(R.string.tv_saturation, progress)
                ivColor.colorFilter = viewModel.adjustSaturation(progress)
            }

            with(viewModel) {
                image.observe(viewLifecycleOwner) { binding.ivColor.setImageBitmap(it) }
            }
        }
    }
}