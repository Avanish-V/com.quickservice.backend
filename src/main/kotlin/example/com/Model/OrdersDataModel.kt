package com.byteapps.serrvicewala.Features.Orders.data


data class OrdersDataModel(

    val orderId: String,
    val userUUID: String,
    val serviceInfo: ServiceInfo,
    val priceTag: PriceDetails,
    val address: UserAddressDataModel,
    val dateTime: DateTime,
    var status: String,
    val professionalID: String,

)

data class ServiceInfo(

    val serviceTitle : String ,
    val serviceId : String ,
)

data class DateTime(

    val date : String,
    val time : String,

)

data class UserAddressDataModel(
    val name: String = "",
    val mobile: String = "",
    val pinCode: String = "",
    val state: String = "",
    val city: String = "",
    val building: String = "",
    val area: String = "",
    val type: String = "",
    val addressId: String = ""
)


data class PriceDetails(
    val price : Int = 0,
    val tax:Int = 0,
    val quantity : Int = 0,
    val total: Int = 0,
    val coupon: Coupon? = null
)

data class Coupon(

    val offerTitle: String,
    val couponCode: String ,
    val discount: Int,

)

enum class Status{

    ACTIVE,
    CANCELED,
    REFUNDED,
    ASSIGNED,
    COMPLETED

}


data class ProfessionalDetails(

    val professionalName:String,
    val professionalImage:String,
    val rating : Int = 0,
    val count : Int = 0

)