package com.example.Authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDataModel(
    @SerialName("userId")val userId: String,
    @SerialName("isFirstUser")val isFirstUser : Boolean,
    @SerialName("userName") val userName : String = "",
    @SerialName("mobile") val mobile : String = "",
    @SerialName("email") val email : String = "",
    @SerialName("gender") val gender : String = "",

)
