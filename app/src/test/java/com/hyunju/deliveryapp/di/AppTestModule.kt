package com.hyunju.deliveryapp.di

import com.google.firebase.auth.FirebaseAuth
import com.hyunju.deliveryapp.data.TestOrderRepository
import com.hyunju.deliveryapp.data.TestRestaurantFoodRepository
import com.hyunju.deliveryapp.data.TestRestaurantRepository
import com.hyunju.deliveryapp.data.entity.LocationLatLngEntity
import com.hyunju.deliveryapp.data.repository.order.OrderRepository
import com.hyunju.deliveryapp.data.repository.resaurant.RestaurantRepository
import com.hyunju.deliveryapp.data.repository.resaurant.food.RestaurantFoodRepository
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import com.hyunju.deliveryapp.screen.order.OrderMenuListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }

    viewModel { (firebaseAuth: FirebaseAuth) -> OrderMenuListViewModel(get(), get(), firebaseAuth) }

    single<RestaurantRepository> { TestRestaurantRepository() }

    single<RestaurantFoodRepository> { TestRestaurantFoodRepository() }

    single<OrderRepository> { TestOrderRepository() }

}