package com.hyunju.deliveryapp.screen.main.home.restaurant.detail

import androidx.annotation.StringRes
import com.hyunju.deliveryapp.R

enum class RestaurantCategoriesDetail(
    @StringRes val categoryNameId: Int
) {

    MENU(R.string.menu), REVIEW(R.string.review)

}