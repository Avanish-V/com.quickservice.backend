import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("userId")val userId: String?,
    @SerialName("isNewUser")val isNewUser: Boolean?,
    @SerialName("status")val status: String? = null,
    @SerialName("reason")val reason: String? = null

)
