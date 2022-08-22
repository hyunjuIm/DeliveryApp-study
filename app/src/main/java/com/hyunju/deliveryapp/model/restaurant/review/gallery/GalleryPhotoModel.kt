package com.hyunju.deliveryapp.model.restaurant.review.gallery

import android.net.Uri
import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.Model

data class GalleryPhotoModel(
    override val id: Long,
    override val type: CellType = CellType.GALLERY_PHOTO_CELL,
    val uri: Uri,
    val name: String,
    val date: String,
    val size: Int,
    val isSelected: Boolean = false
) : Model(id, type)
