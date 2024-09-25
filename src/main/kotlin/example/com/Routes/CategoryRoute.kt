package com.example.Routes

import com.cloudinary.utils.ObjectUtils
import example.com.Repositories.Categories.CategoryModel
import com.example.Repositories.Categories.CategoryRepository
import com.example.cloudinary
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.io.File
import java.sql.SQLException

fun Route.categoryRoute(categoryRepository: CategoryRepository) {

    val logger = LoggerFactory.getLogger("CategoryRoute")

    route("/category") {

        post("/addCategory") {

            val multipart = call.receiveMultipart()
            var title: String? = null
            var id: String? = null
            var imageUrl: String? = null
            val uploadDir = File("upload/products/category")

            if (!uploadDir.exists()) {
                uploadDir.mkdirs()
            }

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {

                        when (part.name) {
                            "title" -> title = part.value
                            "serviceTAG" -> id = part.value
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



            if (title.isNullOrEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "title Missing")
            if (id.isNullOrEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "id Missing")
            if (imageUrl.isNullOrEmpty()) return@post call.respond(HttpStatusCode.BadRequest, "ImageUrl Missing")

            try {

                val category = categoryRepository.insertCategory(CategoryModel(title!!, id!!, imageUrl!!))
                call.respond(HttpStatusCode.OK, "Category Uploaded Successfully to Server: $category")

            } catch (e: Exception) {
                call.respond("Error While Uploading Category To Server: ${e.message}")
            }
        }

        get {
            try {
                val categories = categoryRepository.getCategory()
                call.respond(HttpStatusCode.OK, categories)
            } catch (e: SQLException) {
                logger.error("Database error while retrieving categories", e)
                call.respond(HttpStatusCode.InternalServerError, "Database error occurred")
            } catch (e: Exception) {
                logger.error("Unexpected error while retrieving categories", e)
                call.respond(HttpStatusCode.InternalServerError, "Failed to retrieve categories")
            }
        }

    }
}

