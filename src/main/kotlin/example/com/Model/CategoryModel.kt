package com.example.Model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind

@Serializable
data class CategoryModel(
    @SerialName("categoryTitle") val categoryTitle: String,
    @SerialName("categoryId") val categoryId: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("type") val type: String? = null,

)