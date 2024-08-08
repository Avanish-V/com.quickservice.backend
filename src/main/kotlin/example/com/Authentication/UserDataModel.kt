package com.example.Authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDataModel(
    @SerialName("userId")val userId: String,
    @SerialName("isFirstUser")val isFirstUser : Boolean,
    @SerialName("userName") val userName : String = "",
    @SerialName("userMobile") val userMobile : String = "",
    @SerialName("userEmail") val userEmail : String = "",
    @SerialName("userGender") val userGender : String = "",

)
