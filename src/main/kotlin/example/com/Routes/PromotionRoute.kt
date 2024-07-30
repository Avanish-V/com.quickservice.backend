package com.example.Routes

import Network.Promotion.data.PromotionDataModel
import com.cloudinary.utils.ObjectUtils
import com.example.Repositories.Promotion.PromotionRepository
import com.example.cloudinary
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.text.Normalizer.Form

fun Route.promotionRoute(promotionRepository: PromotionRepository){

    route("/promotion"){

        post("/createPromotion") {

            val multipartPart = call.receiveMultipart()
            var categoryId : String? = null
            var workType : String? = null
            var imageUrl : String? = null

            multipartPart.forEachPart { part->

                when(part){
                    is PartData.FormItem->{

                        when(part.name){

                            "categoryId" -> categoryId = part.value
                            "workType" -> workType = part.value
                        }

                    }
                    is PartData.FileItem->{

                        val byte = part.streamProvider().readBytes()
                        val upload = cloudinary.uploader().upload(byte, ObjectUtils.emptyMap())
                        imageUrl = upload["secure_url"] as String
                    }


                    else -> {}
                }

            }

            if (categoryId == null) return@post call.respond(HttpStatusCode.BadRequest,"category missing")
            if (workType == null) return@post call.respond(HttpStatusCode.BadRequest,"workType missing")
            if (imageUrl == null) return@post call.respond(HttpStatusCode.BadRequest,"imageUrl missing")

            try {

                val response = promotionRepository.createPromotion(promotionDataModel = PromotionDataModel(
                    imageUrl = imageUrl!!,
                    categoryId = categoryId!!,
                    workType = workType!!
                ))
                call.respond(HttpStatusCode.OK,response)


            }catch (e:Exception){

                call.respond(HttpStatusCode.InternalServerError,"Failed to upload:${e.message}")

            }




        }

        get {

            try {

                val categoryList = promotionRepository.getPromotion()
                call.respond(HttpStatusCode.OK,categoryList)

            }catch (e:Exception){
                call.respond(HttpStatusCode.InternalServerError,"failed to fetch result:${e.message}")
            }

        }



    }



}