package com.hyunju.deliveryapp.screen.main.home.restaurant

import androidx.annotation.StringRes
import com.hyunju.deliveryapp.R

enum class RestaurantCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeId: Int,

    ) {

    ALL(R.string.all, R.string.all_type)
}