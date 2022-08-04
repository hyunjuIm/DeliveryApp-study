package com.hyunju.deliveryapp.data.repository.map

import android.util.Log
import com.hyunju.deliveryapp.data.entity.LocationLatLngEntity
import com.hyunju.deliveryapp.data.network.MapApiService
import com.hyunju.deliveryapp.data.reponse.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMapRepository(
    private val mapApiService: MapApiService,
    private val ioDispatcher: CoroutineDispatcher
) : MapRepository {

    override suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ): AddressInfo? = withContext(ioDispatcher) {
        val response = mapApiService.getReverseGeoCode(
            lat = locationLatLngEntity.latitude,
            lon = locationLatLngEntity.longitude
        )
        Log.d("으악", "$response, ${response.body()}, ${response.message()}")
        if (response.isSuccessful) {
            response.body()?.addressInfo
        } else {
            null
        }
    }
}