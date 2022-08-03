package com.hyunju.deliveryapp.screen.main.home

import com.google.android.material.tabs.TabLayoutMediator
import com.hyunju.deliveryapp.databinding.FragmentHomeBinding
import com.hyunju.deliveryapp.screen.base.BaseFragment
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.hyunju.deliveryapp.screen.main.home.restaurant.RestaurantListFragment
import com.hyunju.deliveryapp.widget.adapter.RestaurantListFragmentPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    override fun initViews() {
        super.initViews()

        initViewPager()
    }

    private fun initViewPager() = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList
            )
            viewPager.adapter = viewPagerAdapter
        }
        viewPager.offscreenPageLimit = restaurantCategories.size
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(restaurantCategories[position].categoryNameId)
        }.attach()
    }

    override fun observeData() {

    }

    companion object {

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }
}