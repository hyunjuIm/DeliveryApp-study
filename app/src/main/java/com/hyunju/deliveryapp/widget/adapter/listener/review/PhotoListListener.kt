package com.hyunju.deliveryapp.widget.adapter.listener.review

import com.hyunju.deliveryapp.model.restaurant.review.UriModel
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener

interface PhotoListListener : AdapterListener {

    fun removePhoto(uri: UriModel)
}