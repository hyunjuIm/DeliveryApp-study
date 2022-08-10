package com.hyunju.deliveryapp.widget.adapter.listener.order

import com.hyunju.deliveryapp.model.restaurant.food.FoodModel
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener

interface OrderMenuListListener : AdapterListener {

    fun onRemoveItem(model: FoodModel)

}