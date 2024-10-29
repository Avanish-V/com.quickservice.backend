package example.com.CustomOTPAuth

import com.example.Authentication.UserDataModel
import io.ktor.client.statement.*

interface OtpAuthRepository {

    suspend fun createService(accountSid: String, authToken: String,toPhoneNumber: String) : String

    suspend fun verify(accountSid: String, authToken: String, serviceSid: String,toPhoneNumber: String, code: String) : String

    suspend fun createUser(userDataModel: UserDataModel):Boolean

    suspend fun getUser(mobile:String):UserDataModel?

}