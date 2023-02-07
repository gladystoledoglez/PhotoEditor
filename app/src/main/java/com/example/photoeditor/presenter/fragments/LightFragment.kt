package com.example.photoeditor.presenter.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import com.example.photoeditor.R
import com.example.photoeditor.databinding.FragmentLightBinding
import com.example.photoeditor.extensions.getBitmap
import com.example.photoeditor.extensions.setOnSeekBarProgressChanged
import com.example.photoeditor.extensions.showMenuItem
import com.example.photoeditor.presenter.viewModels.LightViewModel

class LightFragment : BaseFragment() {
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
        setHasOptionsMenu(true)
        viewModel.changeImage(arguments?.getBitmap(MainFragment.LIGHT_IMAGE))
        with(binding) {
            tvBrightness.text = getString(R.string.tv_brightness, 0)
            sbBrightness.setOnSeekBarProgressChanged { _, progress: Int, _ ->
                tvBrightness.text = getString(R.string.tv_brightness, progress)
                ivLight.colorFilter = viewModel.adjustBrightness(progress)
            }
            tvContrast.text = getString(R.string.tv_contrast, 0)
            sbContrast.setOnSeekBarProgressChanged { _, progress: Int, _ ->
                tvContrast.text = getString(R.string.tv_contrast, progress)
                ivLight.colorFilter = viewModel.adjustContrast(progress)
            }
        }
        with(viewModel) {
            image.observe(viewLifecycleOwner) { binding.ivLight.setImageBitmap(it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.showMenuItem(R.id.actionSave)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionSave -> {
            binding.ivLight.drawToBitmap().apply {
                viewModel.changeImage(image = this)
                sendImageFrom(TAG, image = this)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        val TAG = LightFragment::class.simpleName.orEmpty()
    }
}