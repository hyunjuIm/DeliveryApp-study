package com.hyunju.deliveryapp.widget.adapter.listener.review.gallery

import com.hyunju.deliveryapp.model.restaurant.review.gallery.GalleryPhotoModel
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener

interface GalleryListener : AdapterListener {

    fun checkPhotoItem(photo: GalleryPhotoModel)

}