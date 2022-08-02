package com.hyunju.deliveryapp.widget.adapter.viewholder

import com.hyunju.deliveryapp.databinding.ViewholderEmptyBinding
import com.hyunju.deliveryapp.model.Model
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    private val binding: ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<Model>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindViews(model: Model, adapterListener: AdapterListener) = Unit

}