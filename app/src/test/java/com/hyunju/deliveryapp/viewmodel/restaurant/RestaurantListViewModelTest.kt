package com.hyunju.deliveryapp.viewmodel.restaurant

import com.hyunju.deliveryapp.data.entity.LocationLatLngEntity
import com.hyunju.deliveryapp.data.repository.resaurant.RestaurantRepository
import com.hyunju.deliveryapp.model.restaurant.RestaurantModel
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantOrder
import com.hyunju.deliveryapp.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

@ExperimentalCoroutinesApi
internal class RestaurantListViewModelTest : ViewModelTest() {

    /*
    * [RestaurantCategory]
    * [LocationLatLngEntity]
    * */

    private var restaurantCategory = RestaurantCategory.ALL

    private val locationLatLngEntity: LocationLatLngEntity = LocationLatLngEntity(0.0, 0.0)

    private val restaurantListViewModel by inject<RestaurantListViewModel> {
        parametersOf(
            restaurantCategory,
            locationLatLngEntity
        )
    }

    private val restaurantRepository by inject<RestaurantRepository>()

    // 식당 리스트 불러오기
    @Test
    fun `test load restaurant list category ALL`() = runBlockingTest {
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity).map {
                    RestaurantModel(
                        id = it.id,
                        restaurantInfoId = it.restaurantInfoId,
                        restaurantCategory = it.restaurantCategory,
                        restaurantTitle = it.restaurantTitle,
                        restaurantImageUrl = it.restaurantImageUrl,
                        grade = it.grade,
                        reviewCount = it.reviewCount,
                        deliveryTimeRange = it.deliveryTimeRange,
                        deliveryTipRange = it.deliveryTipRange,
                        restaurantTelNumber = it.restaurantTelNumber
                    )
                }
            )
        )
    }

    // 예상치 못하게 식당 카테고리가 바뀐 경우
    @Test
    fun `test load restaurant list category Excepted`() = runBlockingTest {
        restaurantCategory = RestaurantCategory.CAFE_DESSERT

        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                listOf()
            )
        )
    }

    // 정렬(빠른 배달 순)이 잘 되는지
    @Test
    fun `test load restaurant list order by fast delivery type`() = runBlockingTest {
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.setRestaurantOrder(RestaurantOrder.FAST_DELIVERY)

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity)
                    .sortedBy { it.deliveryTimeRange.first }
                    .map {
                        RestaurantModel(
                            id = it.id,
                            restaurantInfoId = it.restaurantInfoId,
                            restaurantCategory = it.restaurantCategory,
                            restaurantTitle = it.restaurantTitle,
                            restaurantImageUrl = it.restaurantImageUrl,
                            grade = it.grade,
                            reviewCount = it.reviewCount,
                            deliveryTimeRange = it.deliveryTimeRange,
                            deliveryTipRange = it.deliveryTipRange,
                            restaurantTelNumber = it.restaurantTelNumber
                        )
                    }
            )
        )
    }

}