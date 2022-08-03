package com.hyunju.deliveryapp.widget.adapter.listener.restaurant

import com.hyunju.deliveryapp.model.restaurant.RestaurantModel
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener

interface RestaurantListListener: AdapterListener {

    fun onClickItem(model: RestaurantModel)
}