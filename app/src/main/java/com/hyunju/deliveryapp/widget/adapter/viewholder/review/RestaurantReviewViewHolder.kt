package com.hyunju.deliveryapp.widget.adapter.viewholder.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.databinding.ViewholderRestaurantReviewBinding
import com.hyunju.deliveryapp.extensions.clear
import com.hyunju.deliveryapp.extensions.load
import com.hyunju.deliveryapp.model.restaurant.RestaurantModel
import com.hyunju.deliveryapp.model.restaurant.review.RestaurantReviewModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener
import com.hyunju.deliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class RestaurantReviewViewHolder(
    private val binding: ViewholderRestaurantReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<RestaurantReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        reviewThumbnailImage.clear()
        reviewThumbnailImage.isGone = true
    }

    override fun bindData(model: RestaurantReviewModel) {
        super.bindData(model)
        with(binding) {
            if (model.thumbnailImageUri != null) {
                reviewThumbnailImage.isVisible = true
                reviewThumbnailImage.load(model.thumbnailImageUri.toString())
            } else {
                reviewThumbnailImage.isGone = true
            }
            reviewTitleText.text = model.title
            reviewText.text = model.description
            ratingBar.rating = model.grade
        }
    }

    override fun bindViews(model: RestaurantReviewModel, adapterListener: AdapterListener) = Unit

}