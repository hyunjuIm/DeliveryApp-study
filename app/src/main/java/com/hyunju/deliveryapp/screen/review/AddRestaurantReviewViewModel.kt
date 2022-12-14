package com.hyunju.deliveryapp.screen.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.data.entity.ReviewEntity
import com.hyunju.deliveryapp.model.restaurant.review.SubmitReviewModel
import com.hyunju.deliveryapp.model.restaurant.review.UriModel
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import com.hyunju.deliveryapp.util.file.FileUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class AddRestaurantReviewViewModel(
    private val restaurantTitle: String,
    private val orderId: String
) : BaseViewModel() {

    private val storage by lazy { FirebaseStorage.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    private var uriList: ArrayList<UriModel> = arrayListOf()

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
            uriList.addAll(
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
        uriList.remove(uri)
        fetchData()
    }

    fun submit(submitReviewModel: SubmitReviewModel) = viewModelScope.launch {
        addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Loading

        if (uriList.isNotEmpty()) {
            val results = uploadPhoto()
            val errorResults = results.filterIsInstance<Pair<Uri, Exception>>()
            val successResults = results.filterIsInstance<String>()

            when {
                // ????????? ??????????????? ???????????? ?????????
                errorResults.isNotEmpty() && successResults.isNotEmpty() -> {
                    addRestaurantReviewStateLiveData.value =
                        AddRestaurantReviewState.Register.Photo(
                            isUploaded = false,
                            submitReviewModel = submitReviewModel.copy(
                                results = results
                            )
                        )
                }
                // ?????? ????????? ???????????? ????????? ??????
                errorResults.isNotEmpty() && successResults.isEmpty() -> {
                    addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Error(
                        R.string.request_error
                    )
                }
                // ????????? ??????
                else -> {
                    addRestaurantReviewStateLiveData.value =
                        AddRestaurantReviewState.Register.Photo(
                            isUploaded = true,
                            submitReviewModel = submitReviewModel.copy(
                                results = successResults
                            )
                        )
                }
            }
        } else {
            addRestaurantReviewStateLiveData.value =
                AddRestaurantReviewState.Register.Photo(
                    isUploaded = true,
                    submitReviewModel = submitReviewModel.copy(
                        results = listOf()
                    )
                )
        }
    }

    private suspend fun uploadPhoto() = withContext(Dispatchers.IO) {
        val uploadDeferred: List<Deferred<Any>> =
            FileUtil.bitmapResize(uriList).mapIndexed { index, uri ->
                viewModelScope.async {
                    try {
                        val fileName = "image_${index}.png"
                        return@async storage
                            .reference
                            .child("article/photo")
                            .child(fileName)
                            .putFile(uri)
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

    fun uploadArticle(submitReviewModel: SubmitReviewModel) {
        addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Loading

        val review = ReviewEntity(
            userId = submitReviewModel.userId,
            title = submitReviewModel.title,
            createdAt = System.currentTimeMillis(),
            content = submitReviewModel.content,
            rating = submitReviewModel.rating,
            imageUrlList = submitReviewModel.results?.map { it.toString() },
            orderId = orderId,
            restaurantTitle = restaurantTitle
        )

        firestore
            .collection("review")
            .add(review)

        addRestaurantReviewStateLiveData.value = AddRestaurantReviewState.Register.Article
    }

}