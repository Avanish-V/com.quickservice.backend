package com.example.Routes

import Network.offers.data.OfferDataModel
import com.example.Repositories.Offer.OfferRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.offerRoute(offerRepository: OfferRepository){

    route("/offer"){

        post ("/create"){

            try {

                val data = call.receive<OfferDataModel>()
                val result = offerRepository.createOffer(data)
                if (result){
                    call.respond(HttpStatusCode.OK,result)
                }else{
                    call.respond(HttpStatusCode.InternalServerError,"Failed to create")
                }

            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message.toString())
            }

        }

        get("/getOffer/{userId}"){

            val userID = call.parameters["userId"] ?: return@get call.respond("UserId is null")

            try {

                val result = offerRepository.getOffer(userID)
                call.respond(HttpStatusCode.OK,result)

            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message.toString())
            }
        }

        get("/getOffer"){

            try {

                val result = offerRepository.getOfferListForAdmin()
                call.respond(HttpStatusCode.OK,result)

            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message.toString())
            }
        }

        get("/getOfferCoupon/{userId}/{productTAG}/{productId}/{workType}"){

            val userID = call.parameters["userId"] ?: return@get call.respond("UserId is null")
            val productTAG = call.parameters["productTAG"] ?: return@get call.respond("productTAG is null")
            val productId = call.parameters["productId"] ?: return@get call.respond("productId is null")
            val workType = call.parameters["workType"] ?: return@get call.respond("workType is null")

            try {

                val result = offerRepository.getOfferCoupons(
                    userId = userID,
                    productTAG = productTAG,
                    productId = productId,
                    workType = workType

                )
                call.respond(HttpStatusCode.OK,result)

            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message.toString())
            }
        }

        patch ("/updateStatus/{promoCode}/{status}"){


            try {

                val id = call.parameters["promoCode"]
                val status = call.parameters["status"]
                val result = offerRepository.updateStatus(status = status.toBoolean(), id = id.toString())
                if (result){
                    call.respond(HttpStatusCode.OK,result)
                }else{
                    call.respond(HttpStatusCode.InternalServerError,"Error")
                }


            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message.toString())

            }
        }

        delete("/delete/{promoCode}"){
            try {
               val id = call.parameters["promoCode"]
                val result = id?.let {
                    it1 -> offerRepository.deleteOffer(it1)
                }
                call.respond(HttpStatusCode.OK,"$result")

            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,"${e.message}")
            }
        }


    }
}