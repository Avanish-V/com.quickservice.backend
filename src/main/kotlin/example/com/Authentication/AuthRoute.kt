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
                    val uid = firebaseToken.uid

                    val user = authenticationRepository.getUser(userId = uid)

                    if (user != null) {

                        call.respond(AuthResponse(userId = user.userId, isNewUser = false))

                    } else {

                        val newUserDataModel = UserDataModel(
                            userId = firebaseToken.uid,
                            isFirstUser = true,
                            userName = "",
                            mobile = "",
                            email = "",
                            gender = ""
                        )

                        authenticationRepository.createUser(newUserDataModel)

                        call.respond(AuthResponse(userId = newUserDataModel.userId, isNewUser = true))

                    }
                } catch (e: Exception) {
                    call.respond(AuthResponse(
                        userId = "",
                        isNewUser = false,
                        status = "failure",
                        reason = e.message
                    ))
                }
            }
        }
    }
}



