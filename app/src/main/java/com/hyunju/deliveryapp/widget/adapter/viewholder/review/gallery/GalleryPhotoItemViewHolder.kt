package com.hyunju.deliveryapp.widget.adapter.viewholder.review.gallery

import androidx.core.content.ContextCompat
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.databinding.ViewholderGalleryPhotoItemBinding
import com.hyunju.deliveryapp.extensions.clear
import com.hyunju.deliveryapp.extensions.load
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.model.restaurant.review.GalleryPhotoModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener
import com.hyunju.deliveryapp.widget.adapter.listener.review.gallery.GalleryListener
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class GalleryPhotoItemViewHolder(
    private val binding: ViewholderGalleryPhotoItemBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<GalleryPhotoModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        photoImageView.clear()
    }

    override fun bindData(model: GalleryPhotoModel) = with(binding) {
        photoImageView.load(model.uri.toString(), 8f)
        checkButton.setImageDrawable(
            ContextCompat.getDrawable(
                binding.root.context,
                if (model.isSelected)
                    R.drawable.ic_check_enabled
                else
                    R.drawable.ic_check_disabled
            )
        )
        if (model.isSelected) {
            checkButton.run {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context, R.drawable.ic_check_enabled
                    )
                )
                setBackgroundResource(R.drawable.bg_round_corner_8_green)
            }
        } else {
            checkButton.run {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context, R.drawable.ic_check_disabled
                    )
                )
                setBackgroundResource(R.drawable.bg_round_corner_8_gray)
            }
        }
    }

    override fun bindViews(model: GalleryPhotoModel, adapterListener: AdapterListener) =
        with(binding) {
            if (adapterListener is GalleryListener) {
                root.setOnClickListener {
                    adapterListener.checkPhotoItem(model)
                }
            }
        }

}