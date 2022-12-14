package com.hyunju.deliveryapp.screen.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.databinding.ActivityMainBinding
import com.hyunju.deliveryapp.screen.main.home.HomeFragment
import com.hyunju.deliveryapp.screen.main.like.RestaurantLikeListFragment
import com.hyunju.deliveryapp.screen.main.my.MyFragment
import com.hyunju.deliveryapp.util.event.MenuChangeEventBus
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        observeData()

        initViews()
    }

    private fun observeData() {
        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect {
                goToTab(it)
            }
        }
    }

    private fun initViews() = with(binding) {
        bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
                true
            }
            R.id.menu_like -> {
                showFragment(
                    RestaurantLikeListFragment.newInstance(),
                    RestaurantLikeListFragment.TAG
                )
                true
            }
            R.id.menu_my -> {
                showFragment(MyFragment.newInstance(), MyFragment.TAG)
                true
            }
            else -> false
        }
    }

    fun goToTab(mainTabMenu: MainTabMenu) {
        binding.bottomNav.selectedItemId = mainTabMenu.menuId
    }

    // commit() ??? transaction ??? ?????? ???????????? ??????, ?????????????????? schedule ?????? ????????????.
    // commit() ??? activity ??? state save ?????? ?????? ??????????????? ?????????, state save ?????? commit() ??? ???????????? exception ??? ????????????.
    // ?????? state save ??? ????????????, fragment ??? state ????????? ???????????? ???????????? ????????? commitAllowingStateLoss() ??? ??????????????? ??????.
    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()
        }
        findFragment?.let {
            // ??????????????? ?????? ????????? ?????? ??????, ???????????????
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: kotlin.run {
            // ??????????????? ?????? ????????? ?????? ??????, ??????
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }
}

enum class MainTabMenu(@IdRes val menuId: Int) {
    HOME(R.id.menu_home), LIKE(R.id.menu_like), MY(R.id.menu_my)
}