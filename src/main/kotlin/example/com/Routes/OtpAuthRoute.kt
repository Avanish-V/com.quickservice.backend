package example.com.Routes

import com.example.Authentication.UserDataModel
import example.com.CustomOTPAuth.OTPVerifyData
import example.com.CustomOTPAuth.OtpAuthRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.otpAuthRoute(authRepository: OtpAuthRepository){


    route("/OtpAuth"){

        val accountSid = "AC651a5fa05b5a1891d758fd8584d45b44"
        val authToken = "eb4775b477810921be2a6248d5535354"

        post ("/generateOTP/{phoneNumber}"){

            try {

                val phoneNumber = call.parameters["phoneNumber"] ?: return@post call.respond("No Mobile")
                val createService = authRepository.createService(accountSid,authToken, toPhoneNumber = phoneNumber)
                call.respond(createService)


            } catch (e: Exception) {
                call.respondText("Failed to create Verify Service: ${e.localizedMessage}", status = HttpStatusCode.InternalServerError)
            }

        }


                post("/verify") {
                    try {
                        // Receive OTP verification data from the request
                        val data = call.receive<OTPVerifyData>()

                        // Verify the OTP using the auth repository
                        val result = authRepository.verify(
                            accountSid = accountSid,
                            authToken = authToken,
                            serviceSid = data.sid,
                            toPhoneNumber = data.phoneNumber,
                            code = data.otp
                        )

                        // If the verification is approved, proceed to get or create the user
                        if (result == "approved") {
                            // Attempt to get the user from the repository using the phone number
                            val user = authRepository.getUser(data.phoneNumber)

                            if (user == null) {
                                // If the user does not exist, create a new user
                                val createUserResponse = authRepository.createUser(
                                    UserDataModel(
                                        userUID = data.sid,
                                        isFirstUser = true,
                                        userMobile = data.phoneNumber
                                    )
                                )
                                // If the user creation is successful, respond with the new user's object ID
                                if (createUserResponse) {
                                    val newUser = authRepository.getUser(data.phoneNumber)
                                    if (newUser != null) {
                                        call.respond(
                                            newUser.userUID.toString()
                                        )
                                    } else {
                                        call.respond(HttpStatusCode.InternalServerError, "User creation failed")
                                    }
                                } else {
                                    call.respond(HttpStatusCode.InternalServerError, "User creation failed")
                                }
                            } else {
                                // If the user already exists, respond with the existing user's object ID
                                call.respond(
                                    user.userUID.toString()
                                )
                            }
                        } else {
                            // If the OTP verification is not approved, respond with an unauthorized status
                            call.respond(HttpStatusCode.Unauthorized, "OTP verification failed")
                        }
                    } catch (e: Exception) {
                        // Handle any exceptions that occur during the process
                        call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.localizedMessage}")
                    }
                }



    }



}