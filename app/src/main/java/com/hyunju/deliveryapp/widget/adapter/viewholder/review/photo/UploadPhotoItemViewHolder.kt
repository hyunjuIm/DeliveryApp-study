package com.hyunju.deliveryapp.widget.adapter.viewholder.review.photo

import com.hyunju.deliveryapp.databinding.ViewholderPhotoItemBinding
import com.hyunju.deliveryapp.extensions.clear
import com.hyunju.deliveryapp.extensions.load
import com.hyunju.deliveryapp.model.restaurant.review.UriModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener
import com.hyunju.deliveryapp.widget.adapter.listener.review.PhotoListListener
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class UploadPhotoItemViewHolder(
    private val binding: ViewholderPhotoItemBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<UriModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        photoImageView.clear()
    }

    override fun bindData(model: UriModel) = with(binding) {
        photoImageView.load(model.uri.toString(), 8f)
    }

    override fun bindViews(model: UriModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is PhotoListListener) {
            closeButton.setOnClickListener {
                adapterListener.removePhoto(model)
            }
        }
    }

}