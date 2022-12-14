package com.hyunju.deliveryapp.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hyunju.deliveryapp.databinding.*
import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.Model
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.viewholder.EmptyViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.food.FoodMenuViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.order.OrderMenuViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.order.OrderViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.restaurant.LikeRestaurantViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.restaurant.RestaurantViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.review.RestaurantReviewViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.review.gallery.GalleryPhotoItemViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.review.photo.PreviewImageViewHolder
import com.hyunju.deliveryapp.widget.adapter.viewholder.review.photo.UploadPhotoItemViewHolder

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
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.LIKE_RESTAURANT_CELL -> LikeRestaurantViewHolder(
                ViewholderLikeRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.REVIEW_CELL -> RestaurantReviewViewHolder(
                ViewholderRestaurantReviewBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.GALLERY_PHOTO_CELL -> GalleryPhotoItemViewHolder(
                ViewholderGalleryPhotoItemBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.PHOTO_URI_CELL -> UploadPhotoItemViewHolder(
                ViewholderPhotoItemBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.PREVIEW_IMAGE_CELL -> PreviewImageViewHolder(
                ViewholderImageBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_FOOD_CELL -> OrderMenuViewHolder(
                ViewholderOrderMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_CELL -> OrderViewHolder(
                ViewholderOrderBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }

}