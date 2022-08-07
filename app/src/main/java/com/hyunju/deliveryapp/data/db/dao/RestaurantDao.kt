package com.hyunju.deliveryapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hyunju.deliveryapp.data.entity.RestaurantEntity

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM RestaurantEntity WHERE restaurantTitle=:title")
    suspend fun get(title: String): RestaurantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inset(restaurantEntity: RestaurantEntity)

    @Query("DELETE FROM RestaurantEntity WHERE restaurantTitle=:title")
    suspend fun delete(title: String)

    @Query("DELETE FROM RestaurantEntity")
    suspend fun deleteAll()

}