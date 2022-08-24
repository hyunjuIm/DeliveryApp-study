package com.hyunju.deliveryapp.model.restaurant.review

import android.net.Uri
import com.hyunju.deliveryapp.model.CellType
import com.hyunju.deliveryapp.model.Model

data class PreviewImageModel(
    override val id: Long,
    override val type: CellType = CellType.PREVIEW_IMAGE_CELL,
    val uri: Uri
) : Model(id, type)
