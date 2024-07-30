package com.example.Routes

import com.example.Model.ReviewDataModel
import com.example.Repositories.Reviews.ReviewRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

fun Route.reviewRoute(reviewRepository: ReviewRepository) {

    route("/reviews") {

        post("/createReview") {

            val reviewData = call.receive<ReviewDataModel>()

            if (reviewData.reviewText.isEmpty()) return@post call.respond(
                HttpStatusCode.BadRequest,
                "Review is missing"
            )
            if (reviewData.reviewBy.isEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "User is missing")
            if (reviewData.serviceProductId.isEmpty()) return@post call.respond(
                HttpStatusCode.BadRequest,
                "ProductId is missing"
            )
            if (reviewData.professionalId.isEmpty()) return@post call.respond(
                HttpStatusCode.BadRequest,
                "professionalId is missing"
            )

            try {

                val result = reviewRepository.createReview(reviewDataModel = reviewData)
                call.respond(HttpStatusCode.OK, result)

            } catch (e: Exception) {

                call.respond(HttpStatusCode.InternalServerError, e.printStackTrace())

            }


        }

        get {

            try {

                val reviewListData = reviewRepository.getAllReviews()
                if (reviewListData.isEmpty()) {
                    call.respond(HttpStatusCode.OK, "No reviews")
                } else {
                    call.respond(HttpStatusCode.OK, reviewListData)
                }

            } catch (e: Exception) {

                call.respond(HttpStatusCode.InternalServerError, e.printStackTrace())

            }

        }

        get("/ratingById/{fieldName}/{id}") {

            val id = call.parameters["id"]
            val fieldName = call.parameters["fieldName"]

            if (id == null) return@get call.respond(HttpStatusCode.BadRequest, "id is missing")
            if (fieldName == null) return@get call.respond(HttpStatusCode.BadRequest, "fieldName is missing")

            try {
                val ratingList = reviewRepository.getRatingById(id = id, fieldName = fieldName)
                if (ratingList.isEmpty()) {
                    call.respond(HttpStatusCode.OK, "Empty list")
                } else {
                    val averageRating = String.format("%.1f", ratingList.average()).toDouble()
                    call.respond(HttpStatusCode.OK, mapOf("averageRating" to averageRating))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message.toString())
            }
        }

        get ("reviewsById/{fieldName}/{id}"){

            val fieldName = call.parameters["fieldName"]
            val id = call.parameters["id"]

            try {
                if (fieldName != null && id != null){
                    val reviewList = reviewRepository.getReviewById(id = id, fieldName = fieldName)
                    call.respond(HttpStatusCode.OK,reviewList)
                }
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message.toString())
            }


        }


    }


}
