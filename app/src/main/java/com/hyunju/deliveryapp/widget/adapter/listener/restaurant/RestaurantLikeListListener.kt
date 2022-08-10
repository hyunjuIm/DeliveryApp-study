package com.hyunju.deliveryapp.widget.adapter.listener.restaurant

import com.hyunju.deliveryapp.model.restaurant.RestaurantModel

interface RestaurantLikeListListener : RestaurantListListener {

    fun onDislikeIte(model: RestaurantModel)

}