package com.hyunju.deliveryapp.screen.review

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.hyunju.deliveryapp.data.entity.UploadPhotoEntity
import com.hyunju.deliveryapp.databinding.ActivityAddRestaurantReviewBinding
import com.hyunju.deliveryapp.model.restaurant.review.UriModel
import com.hyunju.deliveryapp.screen.base.BaseActivity
import com.hyunju.deliveryapp.screen.review.gallery.GalleryActivity
import com.hyunju.deliveryapp.screen.review.photo.CameraActivity
import com.hyunju.deliveryapp.screen.review.photo.preview.ImagePreviewListActivity.Companion.URI_LIST_KEY
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.hyunju.deliveryapp.widget.adapter.listener.review.PhotoListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddRestaurantReviewActivity :
    BaseActivity<AddRestaurantReviewViewModel, ActivityAddRestaurantReviewBinding>() {

    private val auth by inject<FirebaseAuth>()

    private val resourcesProvider by inject<ResourcesProvider>()

    private val restaurantTitle by lazy { intent.getStringExtra(RESTAURANT_TITLE_KEY)!! }
    private val orderId by lazy { intent.getStringExtra(ORDER_ID_KEY)!! }

    companion object {

        const val PERMISSION_REQUEST_CODE = 1000
        const val GALLERY_REQUEST_CODE = 1001
        const val CAMERA_REQUEST_CODE = 1002

        const val RESTAURANT_TITLE_KEY = "restaurantTitle"
        const val ORDER_ID_KEY = "orderId"

        fun newIntent(context: Context, orderId: String, restaurantTitle: String) =
            Intent(context, AddRestaurantReviewActivity::class.java).apply {
                putExtra(ORDER_ID_KEY, orderId)
                putExtra(RESTAURANT_TITLE_KEY, restaurantTitle)
            }
    }

    override val viewModel by viewModel<AddRestaurantReviewViewModel> {
        parametersOf(
            restaurantTitle,
            orderId
        )
    }

    private val adapter by lazy {
        ModelRecyclerAdapter<UriModel, AddRestaurantReviewViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : PhotoListListener {
                override fun removePhoto(uri: UriModel) {
                    viewModel.removePhotoItem(uri)
                }
            }
        )
    }

    override fun getViewBinding(): ActivityAddRestaurantReviewBinding =
        ActivityAddRestaurantReviewBinding.inflate(layoutInflater)

    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener { finish() }

        titleTextView.text = restaurantTitle

        photoRecyclerView.adapter = adapter

        imageAddButton.setOnClickListener {
            showPictureUploadDialog()
        }

        submitButton.setOnClickListener {
            viewModel.submit(
                UploadPhotoEntity(
                    title = binding.titleEditText.text.toString(),
                    content = binding.contentEditText.text.toString(),
                    rating = binding.ratingBar.rating,
                    userId = auth.currentUser?.uid.orEmpty()
                )
            )
        }
    }

    override fun observeData() = viewModel.addRestaurantReviewStateLiveData.observe(this) {
        when (it) {
            is AddRestaurantReviewState.Loading -> handleLoading()
            is AddRestaurantReviewState.Success -> handleSuccess(it)
            is AddRestaurantReviewState.Register -> handleRegister(it)
            is AddRestaurantReviewState.Error -> handleError(it)
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: AddRestaurantReviewState.Success) = with(binding) {
        progressBar.isGone = true
        adapter.submitList(state.uriList)
        adapter.notifyDataSetChanged()
    }

    private fun handleRegister(state: AddRestaurantReviewState.Register) {
        when (state) {
            is AddRestaurantReviewState.Register.Photo -> {
                // 사진 업로드 성공
                if (state.isUploaded) {
                    viewModel.uploadArticle(state.uploadPhotoEntity)
                } else { // 사진 업로드 실패
                    photoUploadErrorButContinueDialog(state.uploadPhotoEntity)
                }
            }

            is AddRestaurantReviewState.Register.Article -> {
                Toast.makeText(this, "리뷰가 성공적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun handleError(state: AddRestaurantReviewState.Error) {
        Toast.makeText(this, getString(state.messageId), Toast.LENGTH_SHORT).show()
    }

    private fun photoUploadErrorButContinueDialog(uploadPhotoEntity: UploadPhotoEntity) {
        uploadPhotoEntity.results?.let {
            val errorResults = uploadPhotoEntity.results.filterIsInstance<Pair<Uri, Exception>>()
            val successResults = uploadPhotoEntity.results.filterIsInstance<String>()

            AlertDialog.Builder(this)
                .setTitle("특정 이미지 업로드 실패")
                .setMessage("업로드에 실패한 이미지가 있습니다." + errorResults.map { (uri, _) ->
                    "$uri\n"
                } + "그럼에도 불구하고 업로드 하시겠습니까?")
                .setPositiveButton("업로드") { _, _ ->
                    viewModel.uploadArticle(uploadPhotoEntity.copy(results = successResults))
                }
                .create()
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGalleryScreen()
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun startGalleryScreen() {
        startActivityForResult( //registerForActivityResult 권유
            GalleryActivity.newIntent(this),
            GALLERY_REQUEST_CODE
        )
    }

    private fun startCameraScreen() {
        startActivityForResult(
            CameraActivity.newIntent(this),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                data?.let { intent ->
                    val uriList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
                    viewModel.addPhotoItemList(uriList)
                } ?: kotlin.run {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            CAMERA_REQUEST_CODE -> {
                data?.let { intent ->
                    val uriList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
                    viewModel.addPhotoItemList(uriList)
                } ?: kotlin.run {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .create()
            .show()
    }

    private fun checkExternalStoragePermission(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup()
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun showPictureUploadDialog() {
        AlertDialog.Builder(this)
            .setTitle("사진 첨부")
            .setMessage("사진 첨부할 방식을 선택해주세요.")
            .setPositiveButton("카메라") { _, _ ->
                checkExternalStoragePermission {
                    startCameraScreen()
                }
            }
            .setNegativeButton("갤러리") { _, _ ->
                checkExternalStoragePermission {
                    startGalleryScreen()
                }
            }
            .create()
            .show()
    }

}