package com.hyunju.deliveryapp.screen.review.photo.preview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.viewpager2.widget.ViewPager2
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.databinding.ActivityImagePreviewListBinding
import com.hyunju.deliveryapp.model.restaurant.review.PreviewImageModel
import com.hyunju.deliveryapp.screen.base.BaseActivity
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.hyunju.deliveryapp.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ImagePreviewListActivity :
    BaseActivity<ImagePreviewListViewModel, ActivityImagePreviewListBinding>() {

    companion object {
        const val URI_LIST_KEY = "uriList"

        fun newIntent(activity: Activity, uriList: List<Uri>) =
            Intent(activity, ImagePreviewListActivity::class.java).apply {
                putExtra(URI_LIST_KEY, ArrayList<Uri>().apply { uriList.forEach { add(it) } })
            }
    }

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<PreviewImageModel, ImagePreviewListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : AdapterListener {}
        )
    }

    override fun getViewBinding(): ActivityImagePreviewListBinding =
        ActivityImagePreviewListBinding.inflate(layoutInflater)

    private val uriList by lazy<List<Uri>> { intent.getParcelableArrayListExtra(URI_LIST_KEY)!! }

    override val viewModel by viewModel<ImagePreviewListViewModel> {
        parametersOf(
            uriList
        )
    }

    override fun initViews() = with(binding) {
        setSupportActionBar(toolbar)

        imageViewPager.adapter = adapter

        deleteButton.setOnClickListener {
            viewModel.removeItem(this@ImagePreviewListActivity, imageViewPager.currentItem)
            binding.indicator.setViewPager(imageViewPager)
        }
    }

    override fun observeData() = viewModel.imagePreviewListStateLiveData.observe(this) {
        bindViews(it)
    }

    private fun bindViews(previewList: MutableList<PreviewImageModel>?) = with(binding) {
        adapter.submitList(previewList?.toList())

        imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                toolbar.title = getString(
                    R.string.images_page,
                    position + 1,
                    previewList?.size
                )
            }
        })

        confirmButton.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(
                    URI_LIST_KEY,
                    ArrayList<Uri>().apply { previewList?.forEach { add(it.uri) } })
            })
            finish()
        }
    }

}