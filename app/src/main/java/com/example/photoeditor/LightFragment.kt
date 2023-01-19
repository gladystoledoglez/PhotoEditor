package com.example.photoeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.photoeditor.databinding.FragmentLightBinding

class LightFragment : Fragment() {
    private lateinit var binding: FragmentLightBinding
    private val viewModel: LightViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.changeImage(arguments?.getBitmap(MainFragment.lightImage))
        with(binding) {
            tvBrightness.text = getString(R.string.tv_brightness, 0)
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(bar: SeekBar?, progress: Int, p2: Boolean) {
                    tvBrightness.text = getString(R.string.tv_brightness, progress)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    // do nothing
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    // do nothing
                }
            })
        }
        with(viewModel) {
            image.observe(viewLifecycleOwner) { binding.ivLight.setImageBitmap(it) }
        }
    }
}