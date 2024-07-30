package com.example.Model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ServiceProductsModel(

    @SerialName("serviceTitle") val serviceTitle:String,
    @SerialName("imageUrl") val imageUrl:String,
    @SerialName("serviceId") val serviceId:String,
    @SerialName("serviceTAG") val serviceTAG:String,
    @SerialName("workType") val workType:String,
    @SerialName("price") val price: Int,
    @SerialName("tax") val tax: Int,
    @SerialName("description") val description: List<Description>,
    @SerialName("rating") val rating : Rating

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