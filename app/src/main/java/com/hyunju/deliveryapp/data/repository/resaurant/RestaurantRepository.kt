package com.hyunju.deliveryapp.data.repository.resaurant

import com.hyunju.deliveryapp.data.entity.RestaurantEntity
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantCategory

interface RestaurantRepository {

    suspend fun getList(
        restaurantCategory: RestaurantCategory,
    ): List<RestaurantEntity>
}