package com.hyunju.deliveryapp.screen.review.gallery

import com.hyunju.deliveryapp.model.restaurant.review.GalleryPhotoModel

sealed class GalleryState {

    object Uninitialized : GalleryState()

    object Loading : GalleryState()

    data class Success(
        val photoList: List<GalleryPhotoModel>
    ) : GalleryState()

    data class Confirm(
        val photoList: List<GalleryPhotoModel>
    ) : GalleryState()

}