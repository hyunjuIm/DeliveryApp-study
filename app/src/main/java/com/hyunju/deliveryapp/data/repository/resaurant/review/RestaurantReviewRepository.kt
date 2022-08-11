package com.hyunju.deliveryapp.data.repository.resaurant.review

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): DefaultRestaurantReviewRepository.Result
}