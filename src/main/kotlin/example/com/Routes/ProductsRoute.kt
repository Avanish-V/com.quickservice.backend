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

        post("/addProduct") {

            val multipartPart = call.receiveMultipart()
            var productTitle: String? = null
            var productImage: String? = null
            var productId: String? = null
            var productTAG: String? = null
            var workType: String? = null
            var price: String? = null
            var serviceTax: String? = null
            var rating:Rating? = null
            var description: List<Description> = listOf()

            multipartPart.forEachPart { part ->

                when (part) {
                    is PartData.FormItem -> {

                        when (part.name) {
                            "productTitle" -> productTitle = part.value
                            "productId" -> productId = part.value
                            "productTAG" -> productTAG = part.value
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
                        productImage = uploadResult["secure_url"] as String

                    }

                    else -> {}
                }
            }

            if (productTitle.isNullOrEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "productTitle is  Missing.")
            if (productTAG.isNullOrEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "productTAG is  Missing.")
            if (productId.isNullOrEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "productId is  Missing.")
            if (price.isNullOrEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "price is  Missing.")
            if (serviceTax.isNullOrEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "serviceTax is  Missing.")
            if (productImage.isNullOrEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "imageUrl is  Missing.")
            if (description.isEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "description is  Missing.")


            try {
                val product = serviceProductRepository.insertProduct(

                    ServiceProductsModel(
                        productTitle = productTitle!!,
                        productImage = productImage.toString(),
                        productId = productId.toString(),
                        productTAG = productTAG.toString(),
                        workType = workType.toString(),
                        price = price!!.toInt(),
                        tax = serviceTax!!.toInt(),
                        description = description,
                        rating = rating!!
                    )
                )

                productImage.let {
                    call.respond(HttpStatusCode.OK, "Product Uploaded Successfully to Server: $product")
                }

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error While Uploading Category To Server: ${e.message}")
            }

        }



        get("/{UUID}/{productTAG}") {
            try {
                val id = call.parameters["productTAG"]
                val userUID = call.parameters["UUID"]

                if (id == null) return@get call.respond(HttpStatusCode.BadRequest,"productTAG")
                if (userUID == null) return@get call.respond(HttpStatusCode.BadRequest,"UUID")

                val getProduct = serviceProductRepository.getServiceProduct(id,userUID)
                call.respond(HttpStatusCode.OK, getProduct)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to retrieve products: ${e.message}")
            }
        }



        get("{productTAG}") {
            try {
                val id = call.parameters["productTAG"] ?: return@get call.respond(HttpStatusCode.BadRequest,"productTAG")

                val getProduct = serviceProductRepository.getServiceProductForAdmin(id)
                call.respond(HttpStatusCode.OK, getProduct)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to retrieve products: ${e.message}")
            }
        }


        delete("/remove/{serviceId}") {

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

