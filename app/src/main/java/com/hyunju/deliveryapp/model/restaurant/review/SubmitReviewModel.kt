package com.hyunju.deliveryapp.model.restaurant.review

data class SubmitReviewModel(
    val results: List<Any>? = null,
    val title: String,
    val content: String,
    val rating: Float,
    val userId: String
)