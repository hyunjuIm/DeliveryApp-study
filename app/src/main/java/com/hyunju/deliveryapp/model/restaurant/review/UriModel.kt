package com.hyunju.deliveryapp.model.restaurant.review

import android.net.Uri
import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.Model

data class UriModel(
    override val id: Long,
    override val type: CellType = CellType.PHOTO_URI_CELL,
    val uri: Uri
) : Model(id, type)