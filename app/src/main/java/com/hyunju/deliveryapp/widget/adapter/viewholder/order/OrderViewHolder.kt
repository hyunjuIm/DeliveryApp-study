package com.hyunju.deliveryapp.widget.adapter.viewholder.order

import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.databinding.ViewholderOrderBinding
import com.hyunju.deliveryapp.extensions.fromNumberToPrice
import com.hyunju.deliveryapp.model.restaurant.order.OrderModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener
import com.hyunju.deliveryapp.widget.adapter.listener.order.OrderListListener
import com.hyunju.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<OrderModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindData(model: OrderModel) = with(binding) {
        super.bindData(model)

        orderTitleText.text = resourcesProvider.getString(
            R.string.order_history_title, model.restaurantTitle
        )

        val foodMenuList = model.foodMenuList
        foodMenuList
            .groupBy { it.title }
            .entries.forEach { (title, menuList) ->
                val orderDataStr =
                    orderContentText.text.toString() +
                            "메뉴 : $title | 가격 : ${menuList.first().price.fromNumberToPrice()}원 * ${menuList.size}\n"
                orderContentText.text = orderDataStr
            }
        orderContentText.text = orderContentText.text.trim()

        orderTotalPriceText.text = resourcesProvider.getString(
            R.string.price,
            foodMenuList.map { it.price }.reduce { total, price -> total + price }
                .fromNumberToPrice()
        )
    }

    override fun bindViews(model: OrderModel, adapterListener: AdapterListener) {
        if (adapterListener is OrderListListener) {
            binding.root.setOnClickListener {
                adapterListener.writeRestaurantReview(model.orderId, model.restaurantTitle)
            }
        }
    }

}