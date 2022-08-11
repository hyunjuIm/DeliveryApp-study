package com.hyunju.deliveryapp.model.restaurant.order

import com.hyunju.deliveryapp.data.entity.OrderEntity
import com.hyunju.deliveryapp.data.entity.RestaurantFoodEntity
import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.Model

data class OrderModel(
    override val id: Long,
    override val type: CellType = CellType.ORDER_CELL,
    val orderId: String,
    val userId: String,
    val restaurantId: Long,
    val foodMenuList: List<RestaurantFoodEntity>,
    val restaurantTitle: String
) : Model(id, type) {

    fun toEntity() = OrderEntity(
        id = orderId,
        userId = userId,
        restaurantId = restaurantId,
        foodMenuList = foodMenuList,
        restaurantTitle = restaurantTitle
    )

}
