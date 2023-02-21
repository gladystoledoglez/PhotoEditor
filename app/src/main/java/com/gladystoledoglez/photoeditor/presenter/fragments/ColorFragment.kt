package com.gladystoledoglez.photoeditor.presenter.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import com.gladystoledoglez.photoeditor.R
import com.gladystoledoglez.photoeditor.databinding.FragmentColorBinding
import com.gladystoledoglez.photoeditor.extensions.getBitmap
import com.gladystoledoglez.photoeditor.extensions.setOnSeekBarProgressChanged
import com.gladystoledoglez.photoeditor.extensions.showMenuItem
import com.gladystoledoglez.photoeditor.presenter.viewModels.ColorViewModel

class ColorFragment : BaseFragment() {
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
        setHasOptionsMenu(true)
        viewModel.changeImage(arguments?.getBitmap(MainFragment.IMAGE))

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.showMenuItem(R.id.actionSave)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionSave -> {
            binding.ivColor.drawToBitmap().apply {
                viewModel.changeImage(image = this)
                sendImageFrom(TAG, image = this)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        val TAG = ColorFragment::class.simpleName.orEmpty()
    }
}