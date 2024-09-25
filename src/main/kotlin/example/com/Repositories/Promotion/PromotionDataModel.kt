package example.com.Repositories.Promotion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PromotionDataModel(
    @SerialName("imageUrl")val imageUrl : String,
    @SerialName("categoryId")val categoryId : String,
    @SerialName("workType")val workType : String
)
