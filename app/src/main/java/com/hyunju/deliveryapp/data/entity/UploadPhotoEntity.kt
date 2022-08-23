package com.hyunju.deliveryapp.data.entity

data class UploadPhotoEntity(
    val results: List<Any>? = null,
    val title: String,
    val content: String,
    val rating: Float,
    val userId: String
)