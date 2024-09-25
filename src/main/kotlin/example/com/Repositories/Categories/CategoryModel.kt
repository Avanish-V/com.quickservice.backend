package example.com.Repositories.Categories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryModel(
    @SerialName("categoryTitle") val categoryTitle: String,
    @SerialName("categoryId") val categoryId: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("type") val type: String? = null,

)