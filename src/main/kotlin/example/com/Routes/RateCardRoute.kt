package com.example.Routes

import Network.Rate.RateCardDataModel
import com.example.Repositories.RateCard.RateCardRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withTimeout

fun Route.rateCardRoute(rateCardRepository: RateCardRepository){

    route("/rateCard"){

        post ("/createRateCard"){

            val data = call.receive<RateCardDataModel>()

            if (data.title.isEmpty()) return@post call.respond("Title is missing")
            if (data.applianceCategory.isEmpty()) return@post call.respond("ApplianceCategory is missing")
            if (data.rateId.isEmpty()) return@post call.respond("Id is missing")

            try {

                val result = rateCardRepository.createRateCard(rateCardDataModel = data)
                call.respond(HttpStatusCode.OK,result)

            }catch (e:Exception){

                call.respond(HttpStatusCode.BadRequest,e.message.toString())

            }

        }

        get("/{applianceCategory}"){

            val applianceCategory = call.parameters["applianceCategory"]

            if (applianceCategory.isNullOrEmpty()) return@get call.respond("applianceCategory is null")

            try {
                val result = withTimeout(5000) { // Set timeout to 5000 milliseconds (5 seconds)
                    rateCardRepository.getRateCardList(applianceCategory = applianceCategory)
                }
                call.respond(HttpStatusCode.OK, result)
            } catch (e: TimeoutCancellationException) {
                call.respond(HttpStatusCode.RequestTimeout, "Request timed out while fetching rate card list")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message.toString())
            }
        }




    }



}