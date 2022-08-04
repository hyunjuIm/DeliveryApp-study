package com.hyunju.deliveryapp.screen.main.home

import androidx.annotation.StringRes
import com.hyunju.deliveryapp.data.entity.MapSearchInfoEntity

sealed class HomeState {

    object Uninitialized : HomeState()

    object Loading : HomeState()

    data class Success(
        val mapSearchInfo: MapSearchInfoEntity,
        val isLocationSame: Boolean
    ) : HomeState()

    data class Error(
        @StringRes val messageId: Int
    ) : HomeState()
}