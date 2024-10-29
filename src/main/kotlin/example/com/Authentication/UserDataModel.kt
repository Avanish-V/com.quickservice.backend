package com.example.Authentication

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


@Serializable
data class UserDataModel(
    @SerialName("userId") val userUID: String? = null,
    @SerialName("isFirstUser") val isFirstUser: Boolean,
    @SerialName("userName") val userName: String? = null,
    @SerialName("userMobile") val userMobile: String = "",
    @SerialName("userEmail") val userEmail: String? = null,
    @SerialName("userGender") val userGender: String? = null
)