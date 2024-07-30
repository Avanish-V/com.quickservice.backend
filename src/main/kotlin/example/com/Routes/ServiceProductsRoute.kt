package com.example.Routes

import com.cloudinary.utils.ObjectUtils
import com.example.Model.Description
import com.example.Model.Rating
import com.example.Model.ServiceProductsModel
import com.example.Repositories.ServiceProducts.ServiceProductRepository
import com.example.cloudinary
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.serviceProductRoute(serviceProductRepository: ServiceProductRepository){



    route("/serviceProducts"){

        post {

            val multipartPart = call.receiveMultipart()
            var serviceTitle: String? = null
            var imageUrl: String? = null
            var serviceId: String? = null
            var serviceTAG: String? = null
            var workType: String? = null
            var price: String? = null
            var serviceTax: String? = null
            var rating:Rating? = null
            var description: List<Description> = listOf()

            multipartPart.forEachPart { part ->

                when (part) {
                    is PartData.FormItem -> {

                        when (part.name) {
                            "serviceTitle" -> serviceTitle = part.value
                            "serviceId" -> serviceId = part.value
                            "serviceTAG" -> serviceTAG = part.value
                            "workType" -> workType = part.value
                            "price" -> price = part.value
                            "serviceTax" -> serviceTax = part.value
                            "rating" -> rating = Json.decodeFromString(part.value)
                            "description" -> description = Json.decodeFromString(part.value)

                        }

                    }
                    is PartData.FileItem -> {

                        val bytes = part.streamProvider().readBytes()
                        val uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap())
                        imageUrl = uploadResult["secure_url"] as String

                    }

                    else -> {}
                }
            }

            try {

                if (serviceTitle == null) return@post call.respond(HttpStatusCode.BadRequest, "serviceTitle is  Missing.")
                if (serviceTAG == null) return@post call.respond(HttpStatusCode.BadRequest, "serviceTAG is  Missing.")
                if (serviceId == null) return@post call.respond(HttpStatusCode.BadRequest, "serviceId is  Missing.")
                if (price == null) return@post call.respond(HttpStatusCode.BadRequest, "price is  Missing.")
                if (serviceTax == null) return@post call.respond(HttpStatusCode.BadRequest, "serviceTax is  Missing.")
                if (imageUrl == null) return@post call.respond(HttpStatusCode.BadRequest, "imageUrl is  Missing.")
                if (description.isEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "description is  Missing.")


                val product = serviceProductRepository.insertProduct(

                    ServiceProductsModel(
                        serviceTitle = serviceTitle!!,
                        imageUrl = imageUrl.toString(),
                        serviceId = serviceId.toString(),
                        serviceTAG = serviceTAG.toString(),
                        workType = workType.toString(),
                        price = price!!.toInt(),
                        tax = serviceTax!!.toInt(),
                        description = description,
                        rating = rating!!
                    )
                )

                imageUrl.let {
                    call.respond(HttpStatusCode.OK, "Product Uploaded Successfully to Server: $product")
                }

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error While Uploading Category To Server: ${e.message}")
            }

        }


        get("/{serviceTAG}") {
            try {
                val id = call.parameters["serviceTAG"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Service TAG not found")
                } else {
                    val getProduct = serviceProductRepository.getServiceProduct(id)
                    call.respond(HttpStatusCode.OK, getProduct)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to retrieve products: ${e.message}")
            }
        }

        delete("/{serviceId}") {

            try {

                val id = call.parameters["serviceId"]
                if (id != null){
                    val deleteResult = serviceProductRepository.deleteProduct(id)
                    call.respond(HttpStatusCode.OK,deleteResult)
                }else{
                    call.respond(HttpStatusCode.BadRequest, "Id not found")
                }



            }catch (e : Exception){
                call.respond(HttpStatusCode.InternalServerError,"Error:"+e.message)
            }
        }



    }



}

