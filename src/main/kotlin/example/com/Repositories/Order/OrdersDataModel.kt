package com.byteapps.serrvicewala.Features.Orders.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OrdersDataModel(
    @SerialName("orderId") val orderId: String,
    @SerialName("userUUID") val userUUID: String,
    @SerialName("productInfo") val productInfo: ProductInfo,
    @SerialName("priceDetails") val priceDetails: PriceDetails,
    @SerialName("address") val address: UserAddressDataModel,
    @SerialName("dateTime") val dateTime: DateTime,
    @SerialName("status") var status: String,
    @SerialName("professionalID") val professionalID: String = "",

    )

@Serializable
data class ProductInfo(
    @SerialName("productTitle") val productTitle: String,
    @SerialName("productId") val productId: String
)

@Serializable
data class DateTime(
    @SerialName("date") val date: String,
    @SerialName("time") val time: String
)

@Serializable
enum class Status {
    @SerialName("Placed") Placed,
    @SerialName("Canceled") Canceled,
    @SerialName("Refunded") Refunded,
    @SerialName("Assigned") Assigned,
    @SerialName("Completed") Completed
}

@Serializable
data class ProfessionalDetails(
    @SerialName("professionalName") val professionalName: String,
    @SerialName("professionalImage") val professionalImage: String,
    @SerialName("rating") val rating: Int = 0,
    @SerialName("count") val count: Int = 0
)

@Serializable
data class UserAddressDataModel(
    @SerialName("name") val name: String = "",
    @SerialName("mobile") val mobile: String = "",
    @SerialName("pinCode") val pinCode: String = "",
    @SerialName("state") val state: String = "",
    @SerialName("city") val city: String = "",
    @SerialName("building") val building: String = "",
    @SerialName("area") val area: String = "",
    @SerialName("type") val type: String = "",
    @SerialName("addressId") val addressId: String = ""
)

@Serializable
data class PriceDetails(
    @SerialName("price") val price: Int,
    @SerialName("tax") val tax: Int,
    @SerialName("quantity") val quantity: Int,
    @SerialName("total") val total: Int,
    @SerialName("coupon") val coupon: Coupon? = null
)

@Serializable
data class Coupon(
    @SerialName("offerTitle") val offerTitle: String,
    @SerialName("couponCode") val couponCode: String,
    @SerialName("discount") val discount: Int
)
