package com.hyunju.deliveryapp.data.repository.map

import com.hyunju.deliveryapp.data.entity.LocationLatLngEntity
import com.hyunju.deliveryapp.data.reponse.address.AddressInfo

interface MapRepository {

    suspend fun getReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity): AddressInfo?

}