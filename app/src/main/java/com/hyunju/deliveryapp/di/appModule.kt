package com.hyunju.deliveryapp.di

import com.hyunju.deliveryapp.screen.main.home.HomeViewModel
import com.hyunju.deliveryapp.screen.main.my.MyViewModel
import com.hyunju.deliveryapp.util.provider.DefaultResourcesProvider
import com.hyunju.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }

    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }

    single { provideRetrofit(get(), get()) }

    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }
}