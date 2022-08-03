package com.hyunju.deliveryapp.screen.main.home

import com.hyunju.deliveryapp.databinding.FragmentHomeBinding
import com.hyunju.deliveryapp.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun observeData() {

    }

    companion object{

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }
}