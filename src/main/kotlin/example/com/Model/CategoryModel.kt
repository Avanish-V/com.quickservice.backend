package com.example.Model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind

@Serializable
data class CategoryModel(
    @SerialName("title") val title: String,
    @SerialName("id") val id: String,
    @SerialName("icon") val icon: String
)