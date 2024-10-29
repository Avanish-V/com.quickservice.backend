package example.com.CustomOTPAuth

import kotlinx.serialization.Serializable

@Serializable
data class VerificationResponse(
    val account_sid: String,
    val amount: String? = null,
    val channel: String,
    val date_created: String,
    val date_updated: String,
    val payee:String? = null,
    val service_sid: String,
    val sid: String,
    val sna_attempts_error_codes: List<Int> = emptyList(),
    val status: String,
    val to: String,
    val valid: Boolean
)