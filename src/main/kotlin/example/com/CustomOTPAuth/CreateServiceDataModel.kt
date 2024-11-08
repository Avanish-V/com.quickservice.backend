package example.com.CustomOTPAuth

import kotlinx.serialization.Serializable

@Serializable
data class CreateServiceDataModel(
    val default_template_sid: String?,
    val tts_name: String?,
    val psd2_enabled: Boolean,
    val do_not_share_warning_enabled: Boolean,
    val mailer_sid: String?,
    val friendly_name: String,
    val url: String,
    val account_sid: String,
    val verify_event_subscription_enabled: Boolean,
    val date_updated: String,
    val totp: Totp,
    val code_length: Int = 4,
    val custom_code_enabled: Boolean,
    val sid: String,
    val push: Push,
    val date_created: String,
    val whatsapp: Whatsapp,
    val dtmf_input_required: Boolean,
    val skip_sms_to_landlines: Boolean,
    val lookup_enabled: Boolean,
    val links: Links
)

@Serializable
data class Totp(
    val time_step: Int,
    val skew: Int,
    val code_length: Int,
    val issuer: String
)

@Serializable
data class Push(
    val apn_credential_sid: String?,
    val include_date: Boolean,
    val fcm_credential_sid: String?
)

@Serializable
data class Whatsapp(
    val from: String?,
    val msg_service_sid: String?
)

@Serializable
data class Links(
    val verification_checks: String,
    val rate_limits: String,
    val entities: String,
    val access_tokens: String,
    val verifications: String,
    val webhooks: String,
    val messaging_configurations: String
)
