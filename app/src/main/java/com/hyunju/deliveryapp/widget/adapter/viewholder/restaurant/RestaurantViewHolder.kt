package com.hyunju.deliveryapp.widget.adapter.viewholder.restaurant

import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.databinding.ViewholderEmptyBinding
import com.hyunju.deliveryapp.databinding.ViewholderRestaurantBinding
import com.hyunju.deliveryapp.extensions.clear
import com.hyunju.deliveryapp.extensions.load
import com.hyunju.deliveryapp.model.restaurant.RestaurantModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener
import com.hyunju.deliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImage.clear()
    }

    override fun bindData(model: RestaurantModel) {
        super.bindData(model)
        with(binding) {
            restaurantImage.load(model.restaurantImageUrl, 24f)
            restaurantTitleText.text = model.restaurantTitle
            gradeText.text = resourcesProvider.getString(R.string.grade_format, model.grade)
            reviewCountText.text =
                resourcesProvider.getString(R.string.review_count, model.reviewCount)

            val (minTime, maxTime) = model.deliveryTimeRange
            deliveryTimeText.text =
                resourcesProvider.getString(R.string.delivery_time, minTime, maxTime)

            val (minTip, maxTip) = model.deliveryTipRange
            resourcesProvider.getString(R.string.delivery_tip, minTip, maxTip)
        }
    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) =
        with(binding) {
            if (adapterListener is RestaurantListListener) {
                root.setOnClickListener {
                    adapterListener.onClickItem(model)
                }
            }
        }
}