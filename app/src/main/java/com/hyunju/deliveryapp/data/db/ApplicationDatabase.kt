package com.hyunju.deliveryapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hyunju.deliveryapp.data.db.dao.FoodMenuBasketDao
import com.hyunju.deliveryapp.data.db.dao.LocationDao
import com.hyunju.deliveryapp.data.db.dao.RestaurantDao
import com.hyunju.deliveryapp.data.entity.LocationLatLngEntity
import com.hyunju.deliveryapp.data.entity.RestaurantEntity
import com.hyunju.deliveryapp.data.entity.RestaurantFoodEntity

@Database(
    entities = [LocationLatLngEntity::class, RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ApplicationDataBase.db"
    }

    abstract fun locationDao(): LocationDao

    abstract fun restaurantDao(): RestaurantDao

    abstract fun foodMenuBasketDao(): FoodMenuBasketDao
}