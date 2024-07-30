package com.example.Routes

import com.example.Repositories.PriceCalculation.PriceCalculationRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.priceCalculationRoute(priceCalculationRepository: PriceCalculationRepository){

    route("/priceCalculation"){

        post ("/{userId}/{serviceProductId}/{couponCode}/{quantity}"){

            val userId = call.parameters["userId"]
            val serviceProductId = call.parameters["serviceProductId"]
            val couponCode = call.parameters["couponCode"]
            val quantity = call.parameters["quantity"]

            if (userId.isNullOrEmpty()) return@post call.respond("userId is null")
            if (serviceProductId.isNullOrEmpty()) return@post call.respond("serviceProductId is null")
            if (couponCode.isNullOrEmpty()) return@post call.respond("couponCode is null")
            if (quantity.isNullOrEmpty()) return@post call.respond("quantity is null")

            try {

                val result = priceCalculationRepository.setPriceWithCoupon(
                    userId = userId,
                    productId = serviceProductId,
                    couponCode = couponCode,
                    quantity = quantity.toInt()
                )
                call.respond(HttpStatusCode.OK,result)

            }catch (e:Exception){

                call.respond(HttpStatusCode.BadRequest,e.message.toString())

            }

        }


    }



}