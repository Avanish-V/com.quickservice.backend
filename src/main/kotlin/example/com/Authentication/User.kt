package com.example.Authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class User(
    @SerialName("idToken")val idToken: String
)
