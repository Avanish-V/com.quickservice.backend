package com.example.Routes

import com.cloudinary.utils.ObjectUtils
import com.example.Model.CategoryModel
import com.example.Repositories.Categories.CategoryRepository
import com.example.cloudinary
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream
import java.sql.SQLException

fun Route.categoryRoute(categoryRepository: CategoryRepository) {

    val logger = LoggerFactory.getLogger("CategoryRoute")

    route("/category") {

        post {
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
                        // Log the form item name and value
                        println("Form item: ${part.name} = ${part.value}")
                        when (part.name) {
                            "title" -> title = part.value
                            "serviceTAG" -> id = part.value // Ensure this name matches the client request
                        }
                    }
                    is PartData.FileItem -> {

                        val bytes = part.streamProvider().readBytes()
                        val uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap())
                        imageUrl = uploadResult["secure_url"] as String


//                        val fileName = part.originalFileName?.replace(" ", "_") ?: "image${System.currentTimeMillis()}"
//                        val file = File(uploadDir, fileName)
//                        part.streamProvider().use { input ->
//                            file.outputStream().buffered().use { output ->
//                                input.copyTo(output)
//                            }
//                        }
//                        imageUrl = "/upload/products/category/${fileName}"
                    }
                    else -> {}
                }
            }



            if (title == null) return@post call.respond(HttpStatusCode.BadRequest, "title Missing")
            if (id == null) return@post call.respond(HttpStatusCode.BadRequest, "id Missing")
            if (imageUrl == null) return@post call.respond(HttpStatusCode.BadRequest, "ImageUrl Missing")

            try {
                val category = categoryRepository.insertCategory(CategoryModel(title!!, id!!, imageUrl!!))
                call.respond(HttpStatusCode.OK, "Category Uploaded Successfully to Server: $category")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error While Uploading Category To Server: ${e.message}")
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

