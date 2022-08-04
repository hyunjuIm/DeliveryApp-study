package com.hyunju.deliveryapp.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.data.entity.LocationLatLngEntity
import com.hyunju.deliveryapp.data.repository.map.MapRepository
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository
) : BaseViewModel() {

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) =
        viewModelScope.launch {
            homeStateLiveData.value = HomeState.Loading
            val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
            addressInfo?.let { info ->
                homeStateLiveData.value = HomeState.Success(
                    mapSearchInfo = info.toSearchInfoEntity(locationLatLngEntity)
                )
            } ?: run {
                homeStateLiveData.value = HomeState.Error(R.string.can_not_load_address_info)
            }
        }

}