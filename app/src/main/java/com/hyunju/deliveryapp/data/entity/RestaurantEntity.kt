package com.hyunju.deliveryapp.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.hyunju.deliveryapp.util.converter.RoomTypeConverters
import kotlinx.parcelize.Parcelize

@Parcelize
@androidx.room.Entity
@TypeConverters(RoomTypeConverters::class)
data class RestaurantEntity(
    override val id: Long,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    @PrimaryKey val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantTelNumber: String?
) : Entity, Parcelable