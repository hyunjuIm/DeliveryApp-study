package com.hyunju.deliveryapp.screen.review.photo.preview

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hyunju.deliveryapp.model.restaurant.review.PreviewImageModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.path.PathUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException

class ImagePreviewListViewModel(
    private val uriList: List<Uri>
) : BaseViewModel() {

    private var previewImageList: MutableList<PreviewImageModel>? = uriList.let {
        uriList.map {
            PreviewImageModel(
                id = it.hashCode().toLong(),
                uri = it
            )
        }.toMutableList()
    }

    val imagePreviewListStateLiveData = MutableLiveData<MutableList<PreviewImageModel>?>()

    override fun fetchData(): Job = viewModelScope.launch {
        imagePreviewListStateLiveData.value = previewImageList
    }

    fun removeItem(context: Context, position: Int) {
        val file = File(
            PathUtil.getPath(context, previewImageList?.get(position)?.uri!!)
                ?: throw FileNotFoundException()
        )
        file.delete()

        previewImageList?.remove(previewImageList?.get(position))

        MediaScannerConnection.scanFile(context, arrayOf(file.path), arrayOf("image/jpeg"), null)

        fetchData()
    }

}