package com.byteapps.savvy.Features.Address.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAddressDataModel(
    @SerialName("name")val name: String = "",
    @SerialName("mobile")val mobile: String = "",
    @SerialName("pinCode")val pinCode: String = "",
    @SerialName("state")val state: String = "",
    @SerialName("city")val city: String = "",
    @SerialName("building")val building: String = "",
    @SerialName("area")val area: String = "",
    @SerialName("type") val type: String = "",
    @SerialName("addressId") val addressId: String = "",
    @SerialName("latlng") val latlng: LatLngAddress? = null
)


@Serializable
data class LatLngAddress(
    @SerialName("latitude")val latitude : Double? = null,
    @SerialName("longitude")val longitude : Double? = null
)