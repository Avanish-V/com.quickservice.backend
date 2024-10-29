package com.example.Response

import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse(
    val token : String
)
