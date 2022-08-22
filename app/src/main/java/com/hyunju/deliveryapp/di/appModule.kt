package com.hyunju.deliveryapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.hyunju.deliveryapp.data.entity.LocationLatLngEntity
import com.hyunju.deliveryapp.data.entity.MapSearchInfoEntity
import com.hyunju.deliveryapp.data.entity.RestaurantEntity
import com.hyunju.deliveryapp.data.entity.RestaurantFoodEntity
import com.hyunju.deliveryapp.data.preference.AppPreferenceManager
import com.hyunju.deliveryapp.data.repository.map.DefaultMapRepository
import com.hyunju.deliveryapp.data.repository.map.MapRepository
import com.hyunju.deliveryapp.data.repository.order.DefaultOrderRepository
import com.hyunju.deliveryapp.data.repository.order.OrderRepository
import com.hyunju.deliveryapp.data.repository.resaurant.DefaultRestaurantRepository
import com.hyunju.deliveryapp.data.repository.resaurant.RestaurantRepository
import com.hyunju.deliveryapp.data.repository.resaurant.food.DefaultRestaurantFoodRepository
import com.hyunju.deliveryapp.data.repository.resaurant.food.RestaurantFoodRepository
import com.hyunju.deliveryapp.data.repository.resaurant.review.DefaultRestaurantReviewRepository
import com.hyunju.deliveryapp.data.repository.resaurant.review.RestaurantReviewRepository
import com.hyunju.deliveryapp.data.repository.user.DefaultUserRepository
import com.hyunju.deliveryapp.data.repository.user.UserRepository
import com.hyunju.deliveryapp.screen.main.home.HomeViewModel
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import com.hyunju.deliveryapp.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.hyunju.deliveryapp.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import com.hyunju.deliveryapp.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import com.hyunju.deliveryapp.screen.main.like.RestaurantLikeListViewModel
import com.hyunju.deliveryapp.screen.main.my.MyViewModel
import com.hyunju.deliveryapp.screen.mylocation.MyLocationViewModel
import com.hyunju.deliveryapp.screen.order.OrderMenuListViewModel
import com.hyunju.deliveryapp.util.event.MenuChangeEventBus
import com.hyunju.deliveryapp.util.provider.DefaultResourcesProvider
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyViewModel(get(), get(), get()) }
    viewModel { RestaurantLikeListViewModel(get()) }

    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(mapSearchInfoEntity, get(), get())
    }
    viewModel { (restaurantEntity: RestaurantEntity) ->
        RestaurantDetailViewModel(restaurantEntity, get(), get())
    }
    viewModel { (restaurantId: Long, restaurantFoodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(restaurantId, restaurantFoodList, get())
    }
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }

    viewModel { OrderMenuListViewModel(get(), get()) }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get(), get()) }
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }

    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }

    single(named("map")) { provideMapRetrofit(get(), get()) }
    single(named("food")) { provideFoodRetrofit(get(), get()) }

    single { provideMapApiService(get(qualifier = named("map"))) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }

    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }
    single { AppPreferenceManager(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }

    single { MenuChangeEventBus() }

    single { Firebase.firestore }
    single { FirebaseAuth.getInstance() }
    single { FirebaseStorage.getInstance() }

}