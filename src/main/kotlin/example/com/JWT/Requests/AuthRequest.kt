package com.example.Requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val userName : String,
    val password:String
)
