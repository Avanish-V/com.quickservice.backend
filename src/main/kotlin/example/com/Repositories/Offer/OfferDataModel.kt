package example.com.Repositories.Offer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferDataModel(
    @SerialName("productTAG") val productTAG : String? = null,
    @SerialName("productTitle") val productTitle : String? = null,
    @SerialName("productId") val productId : String? = null,
    @SerialName("imageUrl") val imageUrl : String,
    @SerialName("discount") val discount : String,
    @SerialName("discountType") val discountType : String,
    @SerialName("workType") val workType : String,
    @SerialName("userType") val userType : String,
    @SerialName("promoCode") val promoCode : String,
    @SerialName("expiration") val expiration : Long? = null,
    @SerialName("appliesTo") val appliesTo : String,
    @SerialName("status") val status : Boolean

)