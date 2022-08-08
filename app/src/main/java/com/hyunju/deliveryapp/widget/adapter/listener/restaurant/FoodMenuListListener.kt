package com.hyunju.deliveryapp.widget.adapter.listener.restaurant

import com.hyunju.deliveryapp.model.restaurant.food.FoodModel
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener

interface FoodMenuListListener : AdapterListener {

    fun onClickItem(model: FoodModel){

    }
}