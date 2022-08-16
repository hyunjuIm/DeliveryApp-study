package com.hyunju.deliveryapp.screen.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.data.repository.order.DefaultOrderRepository
import com.hyunju.deliveryapp.data.repository.order.OrderRepository
import com.hyunju.deliveryapp.data.repository.resaurant.food.RestaurantFoodRepository
import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.restaurant.food.FoodModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderMenuListViewModel(
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val orderRepository: OrderRepository,
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    val orderMenuStateLiveData = MutableLiveData<OrderMenuState>(OrderMenuState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        orderMenuStateLiveData.value = OrderMenuState.Loading
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        orderMenuStateLiveData.value = OrderMenuState.Success(
            foodMenuList.map {
                FoodModel(
                    id = it.hashCode().toLong(),
                    type = CellType.ORDER_FOOD_CELL,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    restaurantId = it.restaurantId,
                    foodId = it.id,
                    restaurantTitle = it.restaurantTitle
                )
            }
        )
    }

    fun clearOrderMenu() = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
        fetchData()
    }

    fun removeOrderMenu(model: FoodModel) = viewModelScope.launch {
        restaurantFoodRepository.removeFoodMenuListInBasket(model.foodId)
        fetchData()
    }

    fun orderMenu() = viewModelScope.launch {
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        if (foodMenuList.isNotEmpty()) {
            val restaurantId = foodMenuList.first().restaurantId
            val restaurantTitle = foodMenuList.first().restaurantTitle
            firebaseAuth.currentUser?.let { user ->
                when (val data = orderRepository.orderMenu(
                    user.uid,
                    restaurantId,
                    foodMenuList,
                    restaurantTitle
                )) {
                    is DefaultOrderRepository.Result.Success<*> -> {
                        restaurantFoodRepository.clearFoodMenuListInBasket()
                        orderMenuStateLiveData.value = OrderMenuState.Order
                    }
                    is DefaultOrderRepository.Result.Error -> {
                        orderMenuStateLiveData.value = OrderMenuState.Error(
                            R.string.request_error, data.e
                        )
                    }
                }
            } ?: run {
                orderMenuStateLiveData.value = OrderMenuState.Error(
                    R.string.user_id_not_found, IllegalAccessError()
                )
            }
        }
    }

}