package com.hyunju.deliveryapp.data.repository.resaurant.food

import com.hyunju.deliveryapp.data.entity.RestaurantFoodEntity

interface RestaurantFoodRepository {

    suspend fun getFoods(restaurantId:Long):List<RestaurantFoodEntity>

}