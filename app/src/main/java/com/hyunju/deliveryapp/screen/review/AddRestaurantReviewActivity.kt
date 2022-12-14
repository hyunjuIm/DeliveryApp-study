package com.hyunju.deliveryapp.screen.review

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.model.restaurant.review.SubmitReviewModel
import com.hyunju.deliveryapp.databinding.ActivityAddRestaurantReviewBinding
import com.hyunju.deliveryapp.model.restaurant.review.UriModel
import com.hyunju.deliveryapp.screen.base.BaseActivity
import com.hyunju.deliveryapp.screen.review.gallery.GalleryActivity
import com.hyunju.deliveryapp.screen.review.photo.CameraActivity
import com.hyunju.deliveryapp.screen.review.photo.preview.ImagePreviewListActivity
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.hyunju.deliveryapp.widget.adapter.listener.review.PhotoListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddRestaurantReviewActivity :
    BaseActivity<AddRestaurantReviewViewModel, ActivityAddRestaurantReviewBinding>() {

    companion object {
        const val RESTAURANT_TITLE_KEY = "restaurantTitle"
        const val ORDER_ID_KEY = "orderId"

        fun newIntent(context: Context, orderId: String, restaurantTitle: String) =
            Intent(context, AddRestaurantReviewActivity::class.java).apply {
                putExtra(ORDER_ID_KEY, orderId)
                putExtra(RESTAURANT_TITLE_KEY, restaurantTitle)
            }
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableArrayListExtra<Uri>(GalleryActivity.URI_LIST_KEY)
                    ?.let { uriList ->
                        viewModel.addPhotoItemList(uriList)
                    } ?: kotlin.run {
                    Toast.makeText(this, R.string.fail_photo_to_get, Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startGalleryScreen()
            } else {
                Toast.makeText(this, R.string.can_not_assigned_permission, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableArrayListExtra<Uri>(ImagePreviewListActivity.URI_LIST_KEY)
                    ?.let { uriList ->
                        viewModel.addPhotoItemList(uriList)
                    } ?: kotlin.run {
                    Toast.makeText(this, R.string.fail_photo_to_get, Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val auth by inject<FirebaseAuth>()

    private val resourcesProvider by inject<ResourcesProvider>()

    private val restaurantTitle by lazy { intent.getStringExtra(RESTAURANT_TITLE_KEY)!! }
    private val orderId by lazy { intent.getStringExtra(ORDER_ID_KEY)!! }

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
            submit()
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
                // ?????? ????????? ??????
                if (state.isUploaded) {
                    viewModel.uploadArticle(state.submitReviewModel)
                } else { // ?????? ????????? ??????
                    photoUploadErrorButContinueDialog(state.submitReviewModel)
                }
            }

            is AddRestaurantReviewState.Register.Article -> {
                Toast.makeText(this, R.string.success_upload_review, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun handleError(state: AddRestaurantReviewState.Error) {
        Toast.makeText(this, getString(state.messageId), Toast.LENGTH_SHORT).show()
    }

    private fun submit() {
        val reviewData = SubmitReviewModel(
            title = binding.titleEditText.text.toString(),
            content = binding.contentEditText.text.toString(),
            rating = binding.ratingBar.rating,
            userId = auth.currentUser?.uid.orEmpty()
        )

        viewModel.submit(reviewData)
    }

    private fun photoUploadErrorButContinueDialog(submitReviewModel: SubmitReviewModel) {
        submitReviewModel.results?.let {
            val errorResults = submitReviewModel.results.filterIsInstance<Pair<Uri, Exception>>()
            val successResults = submitReviewModel.results.filterIsInstance<String>()

            AlertDialog.Builder(this)
                .setTitle("?????? ????????? ????????? ??????")
                .setMessage("???????????? ????????? ???????????? ????????????." + errorResults.map { (uri, _) ->
                    "$uri\n"
                } + "???????????? ???????????? ????????? ???????????????????")
                .setPositiveButton("?????????") { _, _ ->
                    viewModel.uploadArticle(submitReviewModel.copy(results = successResults))
                }
                .create()
                .show()
        }
    }

    private fun startGalleryScreen() {
        galleryLauncher.launch(
            GalleryActivity.newIntent(this)
        )
    }

    private fun startCameraScreen() {
        cameraLauncher.launch(
            CameraActivity.newIntent(this)
        )
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("????????? ???????????????.")
            .setMessage("????????? ???????????? ?????? ???????????????.")
            .setPositiveButton("??????") { _, _ ->
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .create()
            .show()
    }

    private fun showPictureUploadDialog() {
        AlertDialog.Builder(this)
            .setTitle("?????? ??????")
            .setMessage("?????? ????????? ????????? ??????????????????.")
            .setPositiveButton("?????????") { _, _ ->
                checkExternalStoragePermission {
                    startCameraScreen()
                }
            }
            .setNegativeButton("?????????") { _, _ ->
                checkExternalStoragePermission {
                    startGalleryScreen()
                }
            }
            .create()
            .show()
    }

    private fun checkExternalStoragePermission(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup()
            }
            else -> {
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

}