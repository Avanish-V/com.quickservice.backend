package com.example.Authentication

import AuthResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoute(authenticationRepository: AuthenticationRepository) {

    route("/authentication") {

        post("/verify") {

            val request = call.receive<User>()

            request.idToken.let {token->

                try {

                    val firebaseToken: FirebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)

                    if (firebaseToken.uid.isEmpty()) return@post call.respond("Unauthorized User!")

                    val user = authenticationRepository.getUser(userId = firebaseToken.uid)


                    if (user != null) {

                        call.respond(

                            AuthResponse(
                                isVerified = true,
                                userUID = firebaseToken.uid,
                                status = "User Exist!"
                            )

                        )

                    } else {

                        val newUserDataModel = UserDataModel(
                            userId = firebaseToken.uid,
                            isFirstUser = true,
                            userName = "",
                            userMobile = "",
                            userEmail = "",
                            userGender = ""
                        )

                        authenticationRepository.createUser(newUserDataModel)

                        call.respond(

                            AuthResponse(
                                isVerified = true,
                                userUID = firebaseToken.uid,
                                status = "User Created!"
                            )

                        )

                    }

                } catch (e: Exception) {

                    call.respond(

                        AuthResponse(
                            status = "Failed!",
                            reason = e.message
                        )

                    )

                }
            }
        }
    }
}



