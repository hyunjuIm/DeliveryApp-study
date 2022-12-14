package com.hyunju.deliveryapp.screen.review

import androidx.annotation.StringRes
import com.hyunju.deliveryapp.model.restaurant.review.SubmitReviewModel
import com.hyunju.deliveryapp.model.restaurant.review.UriModel

sealed class AddRestaurantReviewState {

    object Uninitialized : AddRestaurantReviewState()

    object Loading : AddRestaurantReviewState()

    data class Success(
        val uriList: ArrayList<UriModel>?
    ) : AddRestaurantReviewState()

    sealed class Register : AddRestaurantReviewState() {

        data class Photo(
            val isUploaded: Boolean,
            val submitReviewModel: SubmitReviewModel
        ) : Register()

        object Article : Register()

    }

    data class Error(
        @StringRes val messageId: Int
    ) : AddRestaurantReviewState()

}