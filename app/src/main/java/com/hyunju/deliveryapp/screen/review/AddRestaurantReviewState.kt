package com.hyunju.deliveryapp.screen.review

import androidx.annotation.StringRes

sealed class AddRestaurantReviewState {

    object Uninitialized : AddRestaurantReviewState()

    object Loading : AddRestaurantReviewState()

    object Success : AddRestaurantReviewState()

    data class Error(
        @StringRes val messageId: Int
    ) : AddRestaurantReviewState()

}