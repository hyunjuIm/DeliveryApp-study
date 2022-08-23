package com.hyunju.deliveryapp.screen.review.photo

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.path.PathUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException

class CameraViewModel : BaseViewModel() {

    val cameraLiveData = MutableLiveData<List<Uri>>()

    private var uriList: MutableList<Uri>? = mutableListOf()

    override fun fetchData(): Job = viewModelScope.launch {
        cameraLiveData.value = uriList?.toList()
    }

    fun addUriItem(context: Context, uri: Uri) {
        val file = File(PathUtil.getPath(context, uri) ?: throw FileNotFoundException())
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.path),
            arrayOf("image/jpeg"),
            null
        )
        uriList?.add(uri)
        fetchData()
    }

}