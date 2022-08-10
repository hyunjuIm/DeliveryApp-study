package com.hyunju.deliveryapp.screen.main.like

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.hyunju.deliveryapp.databinding.FragmentRestaurantLikeListBinding
import com.hyunju.deliveryapp.model.restaurant.RestaurantModel
import com.hyunju.deliveryapp.screen.base.BaseFragment
import com.hyunju.deliveryapp.screen.main.home.restaurant.detail.RestaurantDetailActivity
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import com.hyunju.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.hyunju.deliveryapp.widget.adapter.listener.restaurant.RestaurantLikeListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantLikeListFragment :
    BaseFragment<RestaurantLikeListViewModel, FragmentRestaurantLikeListBinding>() {

    override fun getViewBinding(): FragmentRestaurantLikeListBinding =
        FragmentRestaurantLikeListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantLikeListViewModel>()

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikeListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : RestaurantLikeListListener {

                override fun onDislikeIte(model: RestaurantModel) {
                    viewModel.dislikeRestaurant(model.toEntity())
                }

                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
                    )
                }
            })
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    override fun initViews() {
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>) {
        val isEmpty = restaurantList.isEmpty()
        binding.recyclerView.isGone = isEmpty
        binding.emptyResultTextView.isVisible = isEmpty
        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }

    companion object {

        fun newInstance() = RestaurantLikeListFragment()

        const val TAG = "restaurantLikeListFragment"

    }
}