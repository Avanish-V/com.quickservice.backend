import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(

    @SerialName("isVerified")val isVerified: Boolean? = null,
    @SerialName("userUID")val userUID: String? = null,
    @SerialName("status")val status: String? = null,
    @SerialName("reason")val reason: String? = null
)
