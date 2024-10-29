package example.com.CustomOTPAuth

import com.example.Authentication.UserDataModel
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.*
import io.ktor.client.engine.cio.*
import io.ktor.util.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException

class OtpAuthImplementation (private val db:MongoDatabase): OtpAuthRepository {


    private val httpClient =  HttpClient(CIO)

    val userCollection = db.getCollection<UserDataModel>("Users")

    override suspend fun createService(
        accountSid: String,
        authToken: String,
        toPhoneNumber: String
    ): String {
        return try {
            // Create the verification service
            val createServiceResponse: HttpResponse = httpClient.post("https://verify.twilio.com/v2/Services") {
                headers {
                    append(HttpHeaders.Authorization, "Basic " + Base64.getEncoder().encodeToString("$accountSid:$authToken".toByteArray()))
                    append(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                }
                setBody("FriendlyName=My First Verify Service")
            }

            // Check if service creation was successful
            if (createServiceResponse.status == HttpStatusCode.Created) {
                val responseBody = createServiceResponse.bodyAsText()
                val serviceData = Json.decodeFromString<CreateServiceDataModel>(responseBody)
                println("Service Created Successfully: $responseBody")

                // Send OTP via SMS
                val verificationResponse: HttpResponse = httpClient.submitForm(
                    url = "https://verify.twilio.com/v2/Services/${serviceData.sid}/Verifications",
                    formParameters = Parameters.build {
                        append("To", toPhoneNumber)
                        append("Channel", "sms")
                    }
                ) {
                    headers {
                        append(HttpHeaders.Authorization, "Basic " + Base64.getEncoder().encodeToString("$accountSid:$authToken".toByteArray()))
                    }
                }

                // Return the SID of the created service if verification succeeds
                if (verificationResponse.status == HttpStatusCode.Created) {
                    println("Verification sent successfully to $toPhoneNumber.")
                    serviceData.sid
                } else {
                    println("Failed to send verification: ${verificationResponse.status}")
                    "Failed to send verification"
                }
            } else {
                println("Failed to create service: ${createServiceResponse.status}")
                "Failed to create service"
            }
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
            "Error: ${e.localizedMessage}"
        }
    }


    override suspend fun verify(
        accountSid: String,
        authToken: String,
        serviceSid: String,
        toPhoneNumber: String,
        code: String
    ): String {
        return try {
           
            val response: HttpResponse = httpClient.submitForm(
                url = "https://verify.twilio.com/v2/Services/$serviceSid/VerificationCheck",
                formParameters = Parameters.build {
                    append("To", toPhoneNumber)
                    append("Code", code)
                }
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Basic " + Base64.getEncoder().encodeToString("$accountSid:$authToken".toByteArray()))
                }
            }

            println("Requesting verification for phone number: $toPhoneNumber with code: $code using service: $serviceSid")


            // Check if the response was successful
            if (response.status == HttpStatusCode.OK) {
                val responseBody = response.bodyAsText()
                val serviceData = Json.decodeFromString<VerificationResponse>(responseBody)
                serviceData.status


            } else {
                // Throw an exception if the status code is not 200
                throw Exception("Verification failed with status: ${response.status.value}")
            }
        } catch (e: IOException) {
            // Handle network-related issues by throwing an exception
            throw IOException("Network error: ${e.localizedMessage}", e)
        } catch (e: Exception) {
            // Throw any other exceptions encountered
            throw Exception("Error: ${e.localizedMessage}", e)
        }
    }

    override suspend fun createUser(userDataModel: UserDataModel): Boolean {
        return userCollection.insertOne(userDataModel).wasAcknowledged()
    }

    override suspend fun getUser(mobile: String): UserDataModel? {
        return userCollection.find(eq ("userMobile",mobile)).firstOrNull()
    }

}