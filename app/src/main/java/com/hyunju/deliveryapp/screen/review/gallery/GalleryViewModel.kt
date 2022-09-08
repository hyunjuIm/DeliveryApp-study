package com.hyunju.deliveryapp.screen.review.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hyunju.deliveryapp.DeliveryApplication.Companion.appContext
import com.hyunju.deliveryapp.model.restaurant.review.GalleryPhotoModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GalleryViewModel : BaseViewModel() {

    private val galleryPhotoRepository by lazy { GalleryPhotoRepository(appContext!!) }

    private lateinit var photoList: MutableList<GalleryPhotoModel>

    val galleryStateLiveData = MutableLiveData<GalleryState>(GalleryState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        galleryStateLiveData.value = GalleryState.Loading
        photoList = galleryPhotoRepository.getAllPhotos()
        galleryStateLiveData.value = GalleryState.Success(
            photoList = photoList
        )
    }

    fun selectPhoto(galleryPhotoModel: GalleryPhotoModel) {
        photoList.find { it.id == galleryPhotoModel.id }?.let { photo ->
            photoList[photoList.indexOf(photo)] = photo.copy(isSelected = photo.isSelected.not())
            galleryStateLiveData.value = GalleryState.Success(
                photoList = photoList
            )
        }
    }

    fun confirmCheckedPhotos() {
        galleryStateLiveData.value = GalleryState.Loading
        galleryStateLiveData.value = GalleryState.Confirm(
            photoList = photoList.filter { it.isSelected }
        )
    }

}