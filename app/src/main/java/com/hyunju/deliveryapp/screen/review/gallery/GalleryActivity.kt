package com.hyunju.deliveryapp.screen.review.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.databinding.ActivityGalleryBinding
import com.hyunju.deliveryapp.model.restaurant.review.GalleryPhotoModel
import com.hyunju.deliveryapp.screen.base.BaseActivity
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.hyunju.deliveryapp.widget.adapter.listener.review.gallery.GalleryListener
import org.koin.android.ext.android.inject

class GalleryActivity : BaseActivity<GalleryViewModel, ActivityGalleryBinding>() {

    companion object {
        fun newIntent(activity: Activity) = Intent(activity, GalleryActivity::class.java)

        const val URI_LIST_KEY = "uriList"
    }

    override val viewModel by viewModels<GalleryViewModel>()

    override fun getViewBinding(): ActivityGalleryBinding =
        ActivityGalleryBinding.inflate(layoutInflater)

    private val resourcesProvider: ResourcesProvider by inject()

    private val adapter by lazy {
        ModelRecyclerAdapter<GalleryPhotoModel, GalleryViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : GalleryListener {
                override fun checkPhotoItem(photo: GalleryPhotoModel) {
                    viewModel.selectPhoto(photo)
                }
            })
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            GridDividerDecoration(
                this@GalleryActivity,
                R.drawable.bg_frame_gallery
            )
        )
        confirmButton.setOnClickListener {
            viewModel.confirmCheckedPhotos()
        }
    }

    override fun observeData() = viewModel.galleryStateLiveData.observe(this) {
        when (it) {
            is GalleryState.Loading -> handleLoading()
            is GalleryState.Success -> handleSuccess(it)
            is GalleryState.Confirm -> handleConfirm(it)
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
        recyclerView.isGone = true
    }

    private fun handleSuccess(state: GalleryState.Success) = with(binding) {
        progressBar.isGone = true
        recyclerView.isVisible = true

        adapter.submitList(state.photoList)
        adapter.notifyDataSetChanged()
    }

    private fun handleConfirm(state: GalleryState.Confirm) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(URI_LIST_KEY, ArrayList(state.photoList.map { it.uri }))
        })
        finish()
    }

}