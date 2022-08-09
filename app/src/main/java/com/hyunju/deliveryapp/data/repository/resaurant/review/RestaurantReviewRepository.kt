package com.hyunju.deliveryapp.data.repository.resaurant.review

import com.hyunju.deliveryapp.data.entity.RestaurantReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String):List<RestaurantReviewEntity>
}