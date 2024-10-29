package example.com.CustomOTPAuth

import kotlinx.serialization.Serializable

@Serializable
data class OTPVerifyData(
    val phoneNumber:String,
    val sid:String,
    val otp:String
)
