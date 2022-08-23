package com.hyunju.deliveryapp.widget.adapter.viewholder.review.photo

import com.hyunju.deliveryapp.databinding.ViewholderImageBinding
import com.hyunju.deliveryapp.extensions.clear
import com.hyunju.deliveryapp.extensions.load
import com.hyunju.deliveryapp.model.restaurant.review.PreviewImageModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class PreviewImageViewHolder(
    private val binding: ViewholderImageBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<PreviewImageModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        imageView.clear()
    }

    override fun bindData(model: PreviewImageModel) = with(binding) {
        imageView.load(model.uri.toString())
    }

    override fun bindViews(model: PreviewImageModel, adapterListener: AdapterListener) {

    }

}