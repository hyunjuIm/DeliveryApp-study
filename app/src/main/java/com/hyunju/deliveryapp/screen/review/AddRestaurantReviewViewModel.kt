package com.hyunju.deliveryapp.screen.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.data.entity.ReviewEntity
import com.hyunju.deliveryapp.data.entity.UploadPhotoEntity
import com.hyunju.deliveryapp.model.restaurant.review.UriModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class AddRestaurantReviewViewModel(
    private val restaurantTitle: String,
    private val orderId: String
) : BaseViewModel() {

    private val storage by lazy { FirebaseStorage.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    private var uriList: ArrayList<UriModel>? = arrayListOf()

    val addRestaurantReviewStateLiveData =
        MutableLiveData<AddRestaurantReviewState>(AddRestaurantReviewState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Loading
        addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Success(
            uriList = uriList
        )
    }

    fun addPhotoItemList(addList: ArrayList<Uri>?) {
        addList?.let { list ->
            uriList?.addAll(
                list.map {
                    UriModel(
                        id = it.hashCode().toLong(),
                        uri = it
                    )
                }
            )
        }
        fetchData()
    }

    fun removePhotoItem(uri: UriModel) {
        uriList?.remove(uri)
        fetchData()
    }

    fun submit(uploadPhotoEntity: UploadPhotoEntity) = viewModelScope.launch {
        addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Loading

        if (uriList?.isNotEmpty() == true) {
            val results = uploadPhoto()
            val errorResults = results.filterIsInstance<Pair<Uri, Exception>>()
            val successResults = results.filterIsInstance<String>()

            when {
                // 에러는 발생했지만 업로드는 성공적
                errorResults.isNotEmpty() && successResults.isNotEmpty() -> {
                    addRestaurantReviewStateLiveData.value =
                        AddRestaurantReviewState.Register.Photo(
                            isUploaded = false,
                            uploadPhotoEntity = uploadPhotoEntity.copy(
                                results = results
                            )
                        )
                }
                // 에러 발생은 안했지만 업로드 실패
                errorResults.isNotEmpty() && successResults.isEmpty() -> {
                    addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Error(
                        R.string.request_error
                    )
                }
                // 업로드 성공
                else -> {
                    addRestaurantReviewStateLiveData.value =
                        AddRestaurantReviewState.Register.Photo(
                            isUploaded = true,
                            uploadPhotoEntity = uploadPhotoEntity.copy(
                                results = successResults
                            )
                        )
                }
            }
        } else {
            addRestaurantReviewStateLiveData.value =
                AddRestaurantReviewState.Register.Photo(
                    isUploaded = true,
                    uploadPhotoEntity = uploadPhotoEntity.copy(
                        results = listOf()
                    )
                )
        }
    }

    private suspend fun uploadPhoto() = withContext(Dispatchers.IO) {
        val uploadDeferred: List<Deferred<Any>> = uriList!!.mapIndexed { index, uri ->
            viewModelScope.async {
                try {
                    val fileName = "image_${index}.png"
                    return@async storage
                        .reference
                        .child("article/photo")
                        .child(fileName)
                        .putFile(uri.uri)
                        .await()
                        .storage
                        .downloadUrl
                        .await()
                        .toString()
                } catch (e: Exception) {
                    addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Error(
                        R.string.request_error
                    )
                    return@async Pair(uri, e)
                }
            }
        }
        return@withContext uploadDeferred.awaitAll()
    }

    fun uploadArticle(uploadPhotoEntity: UploadPhotoEntity) {
        addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Loading

        val review = ReviewEntity(
            userId = uploadPhotoEntity.userId,
            title = uploadPhotoEntity.title,
            createdAt = System.currentTimeMillis(),
            content = uploadPhotoEntity.content,
            rating = uploadPhotoEntity.rating,
            imageUrlList = uploadPhotoEntity.results?.map { it.toString() },
            orderId = orderId,
            restaurantTitle = restaurantTitle
        )

        firestore
            .collection("review")
            .add(review)

        addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Register.Article
    }

}