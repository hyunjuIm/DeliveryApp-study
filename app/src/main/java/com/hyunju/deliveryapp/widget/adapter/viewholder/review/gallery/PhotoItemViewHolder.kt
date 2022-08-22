package com.hyunju.deliveryapp.widget.adapter.viewholder.review.gallery

import android.util.Log
import androidx.core.content.ContextCompat
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.databinding.ViewholderGalleryPhotoItemBinding
import com.hyunju.deliveryapp.extensions.clear
import com.hyunju.deliveryapp.extensions.load
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.screen.main.like.RestaurantLikeListFragment.Companion.TAG
import com.hyunju.deliveryapp.model.restaurant.review.gallery.GalleryPhotoModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener
import com.hyunju.deliveryapp.widget.adapter.listener.review.gallery.GalleryListener
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class PhotoItemViewHolder(
    private val binding: ViewholderGalleryPhotoItemBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<GalleryPhotoModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        photoImageView.clear()
    }

    override fun bindData(model: GalleryPhotoModel) = with(binding) {
        photoImageView.load(model.uri.toString(), 8f)
        Log.d(TAG, "현재 isSelected: ${model.isSelected}")
        checkButton.setImageDrawable(
            ContextCompat.getDrawable(
                binding.root.context,
                if (model.isSelected)
                    R.drawable.ic_check_enabled
                else
                    R.drawable.ic_check_disabled
            )
        )
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