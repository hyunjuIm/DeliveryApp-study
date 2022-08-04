package com.hyunju.deliveryapp.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hyunju.deliveryapp.R
import com.hyunju.deliveryapp.data.entity.LocationLatLngEntity
import com.hyunju.deliveryapp.data.entity.MapSearchInfoEntity
import com.hyunju.deliveryapp.data.repository.map.MapRepository
import com.hyunju.deliveryapp.data.repository.user.UserRepository
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    companion object {
        const val MY_LOCATION_KEY = "MyLocation"
    }

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) =
        viewModelScope.launch {
            homeStateLiveData.value = HomeState.Loading
            val userLocation = userRepository.getUserLocation()
            val currentLocation = userLocation ?: locationLatLngEntity

            val addressInfo = mapRepository.getReverseGeoInformation(currentLocation)
            addressInfo?.let { info ->
                homeStateLiveData.value = HomeState.Success(
                    mapSearchInfo = info.toSearchInfoEntity(locationLatLngEntity),
                    isLocationSame = currentLocation == locationLatLngEntity
                )
            } ?: run {
                homeStateLiveData.value = HomeState.Error(R.string.can_not_load_address_info)
            }
        }

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        return when (val data = homeStateLiveData.value) {
            is HomeState.Success -> {
                data.mapSearchInfo
            }
            else -> null
        }
    }
}