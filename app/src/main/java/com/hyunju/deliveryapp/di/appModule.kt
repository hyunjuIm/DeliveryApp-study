package com.hyunju.deliveryapp.di

import com.hyunju.deliveryapp.data.entity.LocationLatLngEntity
import com.hyunju.deliveryapp.data.entity.MapSearchInfoEntity
import com.hyunju.deliveryapp.data.entity.RestaurantEntity
import com.hyunju.deliveryapp.data.repository.map.DefaultMapRepository
import com.hyunju.deliveryapp.data.repository.map.MapRepository
import com.hyunju.deliveryapp.data.repository.resaurant.DefaultRestaurantRepository
import com.hyunju.deliveryapp.data.repository.resaurant.RestaurantRepository
import com.hyunju.deliveryapp.data.repository.user.DefaultUserRepository
import com.hyunju.deliveryapp.data.repository.user.UserRepository
import com.hyunju.deliveryapp.screen.main.home.HomeViewModel
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import com.hyunju.deliveryapp.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.hyunju.deliveryapp.screen.main.my.MyViewModel
import com.hyunju.deliveryapp.screen.mylocation.MyLocationViewModel
import com.hyunju.deliveryapp.util.provider.DefaultResourcesProvider
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(), get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(mapSearchInfoEntity, get(), get())
    }
    viewModel { (restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity) }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get()) }

    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }

    single { provideMapRetrofit(get(), get()) }

    single { provideMapApiService(get()) }

    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }

    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }
}