package com.example.Model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ServiceProductsModel(

    @SerialName("productTitle") val productTitle:String,
    @SerialName("productImage") val productImage:String,
    @SerialName("productId") val productId:String,
    @SerialName("productTAG") val productTAG:String,
    @SerialName("workType") val workType:String,
    @SerialName("price") val price: Int,
    @SerialName("tax") val tax: Int,
    @SerialName("description") val description: List<Description>,
    @SerialName("rating") val rating : Rating,
    @SerialName("offerAvailable") val offerAvailable : Int? = null

)


@Serializable
data class Description(
    @SerialName("title")val title :String = "",
    @SerialName("comment") val comment : String = ""
)

@Serializable
data class Rating(
    @SerialName("rating")val rating : String = "0",
    @SerialName("count")val count : String = "0",

)