package com.hyunju.deliveryapp.di

import android.content.Context
import androidx.room.Room
import com.hyunju.deliveryapp.data.db.ApplicationDatabase

fun provideDB(context: Context): ApplicationDatabase =
    Room.databaseBuilder(context, ApplicationDatabase::class.java, ApplicationDatabase.DB_NAME)
        .build()

fun provideLocationDao(database: ApplicationDatabase) = database.locationDao()