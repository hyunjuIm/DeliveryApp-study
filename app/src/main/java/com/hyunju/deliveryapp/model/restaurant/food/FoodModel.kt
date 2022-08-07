package com.hyunju.deliveryapp.model.restaurant.food

import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long
) : Model(id, type) {

}