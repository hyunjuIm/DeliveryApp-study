package com.hyunju.deliveryapp.model.restaurant.food

import com.hyunju.deliveryapp.data.entity.RestaurantFoodEntity
import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val foodId: String
) : Model(id, type) {

    // basketIndex : 한 메뉴에 여러개를 담게 하기 위해
    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}", title, description, price, imageUrl, restaurantId
    )
}