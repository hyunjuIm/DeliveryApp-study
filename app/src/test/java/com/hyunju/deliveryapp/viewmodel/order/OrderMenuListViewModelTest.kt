package com.hyunju.deliveryapp.viewmodel.order

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hyunju.deliveryapp.data.entity.OrderEntity
import com.hyunju.deliveryapp.data.entity.RestaurantFoodEntity
import com.hyunju.deliveryapp.data.repository.order.DefaultOrderRepository
import com.hyunju.deliveryapp.data.repository.order.OrderRepository
import com.hyunju.deliveryapp.data.repository.resaurant.food.RestaurantFoodRepository
import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.restaurant.food.FoodModel
import com.hyunju.deliveryapp.screen.order.OrderMenuListViewModel
import com.hyunju.deliveryapp.screen.order.OrderMenuState
import com.hyunju.deliveryapp.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class OrderMenuListViewModelTest : ViewModelTest() {

    @Mock
    lateinit var firebaseAuth: FirebaseAuth

    @Mock
    lateinit var firebaseUser: FirebaseUser

    private val orderMenuListViewModel by inject<OrderMenuListViewModel> {
        parametersOf(firebaseAuth)
    }

    private val restaurantFoodRepository by inject<RestaurantFoodRepository>()

    private val orderRepository by inject<OrderRepository>()

    private val restaurantId = 0L
    private val restaurantTitle = "식당명"


    // 장바구니 메뉴를 담는다.
    @Test
    fun `insert food menus in basket`() = runBlockingTest {
        (0 until 10).forEach {
            restaurantFoodRepository.insertFoodMenuInBasket(
                RestaurantFoodEntity(
                    id = it.toString(),
                    title = "메뉴 $it",
                    description = "소개 $it",
                    price = it,
                    imageUrl = "",
                    restaurantId = restaurantId,
                    restaurantTitle = restaurantTitle
                )
            )
        }
        assert(restaurantFoodRepository.getAllFoodMenuListInBasket().size == 10)
    }

    // 장바구니에 담은 메뉴를 리스트로 뿌려준다.
    @Test
    fun `test load order menu list`() = runBlockingTest {
        `insert food menus in basket`()

        val testObservable = orderMenuListViewModel.orderMenuStateLiveData.test()

        orderMenuListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                OrderMenuState.Uninitialized,
                OrderMenuState.Loading,
                OrderMenuState.Success(
                    restaurantFoodModelList =
                    restaurantFoodRepository.getAllFoodMenuListInBasket().map {
                        FoodModel(
                            id = it.hashCode().toLong(),
                            type = CellType.ORDER_FOOD_CELL,
                            title = it.title,
                            description = it.description,
                            price = it.price,
                            imageUrl = it.imageUrl,
                            restaurantId = restaurantId,
                            foodId = it.id,
                            restaurantTitle = it.restaurantTitle
                        )
                    }
                )
            )
        )
    }

    // 장바구니 목록에 있는 데이터를 가지고 주문을 한다.
    @Test
    fun `test do order menu list`() = runBlockingTest {
        `insert food menus in basket`()

        val userId = "hyunju"
        Mockito.`when`(firebaseAuth.currentUser).then { firebaseUser }
        Mockito.`when`(firebaseUser.uid).then { userId }

        val testObservable = orderMenuListViewModel.orderMenuStateLiveData.test()

        orderMenuListViewModel.fetchData()

        val menuListInBasket =
            restaurantFoodRepository.getAllFoodMenuListInBasket().map { it.copy() }

        val menuListInBasketModelList = menuListInBasket.map {
            FoodModel(
                id = it.hashCode().toLong(),
                type = CellType.ORDER_FOOD_CELL,
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = restaurantId,
                foodId = it.id,
                restaurantTitle = it.restaurantTitle
            )
        }

        orderMenuListViewModel.orderMenu()

        testObservable.assertValueSequence(
            listOf(
                OrderMenuState.Uninitialized,
                OrderMenuState.Loading,
                OrderMenuState.Success(
                    restaurantFoodModelList = menuListInBasketModelList
                ),
                OrderMenuState.Order
            )
        )

        assert(orderRepository.getAllOrderMenus(userId) is DefaultOrderRepository.Result.Success<*>)

        val result =
            (orderRepository.getAllOrderMenus(userId) as DefaultOrderRepository.Result.Success<*>).data
        assert(
            result?.equals(
                listOf(
                    OrderEntity(
                        id = 0.toString(),
                        userId = userId,
                        restaurantId = restaurantId,
                        foodMenuList = menuListInBasket,
                        restaurantTitle = restaurantTitle
                    )
                )
            ) ?: false
        )
    }

}