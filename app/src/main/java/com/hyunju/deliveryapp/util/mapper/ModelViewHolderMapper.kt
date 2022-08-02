package com.hyunju.deliveryapp.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hyunju.deliveryapp.databinding.ViewholderEmptyBinding
import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.Model
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.viewholder.EmptyViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }

}