package com.hyunju.deliveryapp.widget.adapter.viewholder.restaurant

import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.databinding.ViewholderLikeRestaurantBinding
import com.hyunju.deliveryapp.extensions.clear
import com.hyunju.deliveryapp.extensions.load
import com.hyunju.deliveryapp.model.restaurant.RestaurantModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener
import com.hyunju.deliveryapp.widget.adapter.listener.restaurant.RestaurantLikeListListener
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class LikeRestaurantViewHolder(
    private val binding: ViewholderLikeRestaurantBinding,
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
            deliveryTipText.text =
                resourcesProvider.getString(R.string.delivery_tip, minTip, maxTip)
        }
    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) =
        with(binding) {
            if (adapterListener is RestaurantLikeListListener) {
                root.setOnClickListener {
                    adapterListener.onClickItem(model)
                }
                likeImageButton.setOnClickListener {
                    adapterListener.onDislikeIte(model)
                }
            }
        }
}