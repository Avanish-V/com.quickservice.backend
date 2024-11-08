
import io.ktor.http.*
import io.ktor.http.content.*
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.Model.AccountStatus
import com.example.Model.ProfessionalDataModel
import com.example.Model.Status
import com.example.Repositories.Professionals.ProfessionalInterface
import com.example.cloudinary
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.professionalRoute(professionalInterface: ProfessionalInterface) {

    route("/professionals") {

        post("/createProfile") {

            val multipart = try {
                call.receiveMultipart()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid multipart data")
                return@post
            }

            var professionalId: String? = null
            var professionalName: String? = null
            var photoUrl: String? = null
            var mobile: String? = null
            var email: String? = null
            var adharNumber: String? = null
            var address: String? = null
            var profession: String? = null


            try {
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "professionalId" -> professionalId = part.value
                                "professionalName" -> professionalName = part.value
                                "mobile" -> mobile = part.value
                                "email" -> email = part.value
                                "adharNumber" -> adharNumber = part.value
                                "address" -> address = part.value
                                "profession" -> profession = part.value
                            }
                        }
                        is PartData.FileItem -> {
                            val byteData = part.streamProvider().readBytes()
                            val upload = cloudinary.uploader().upload(byteData, ObjectUtils.asMap("folder", "Professionals"))
                            photoUrl = upload["secure_url"] as? String
                        }
                        else -> {}
                    }
                    part.dispose()
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error processing multipart data: ${e.localizedMessage}")
                return@post
            }

            if (professionalId.isNullOrBlank()) return@post call.respond(HttpStatusCode.BadRequest, "professionalId Missing")
            if (professionalName.isNullOrBlank()) return@post call.respond(HttpStatusCode.BadRequest, "professionalName Missing")
            if (mobile.isNullOrBlank()) return@post call.respond(HttpStatusCode.BadRequest, "mobile Missing")
            if (email.isNullOrBlank()) return@post call.respond(HttpStatusCode.BadRequest, "email Missing")
            if (adharNumber.isNullOrBlank()) return@post call.respond(HttpStatusCode.BadRequest, "adharNumber Missing")
            if (address.isNullOrBlank()) return@post call.respond(HttpStatusCode.BadRequest, "address Missing")
            if (profession.isNullOrBlank()) return@post call.respond(HttpStatusCode.BadRequest, "profession Missing")
            if (photoUrl.isNullOrBlank()) return@post call.respond(HttpStatusCode.BadRequest, "PhotoUrl Missing")

            try {
                val professionalData = ProfessionalDataModel(
                    professionalId = professionalId!!,
                    professionalName = professionalName!!,
                    photoUrl = photoUrl!!,
                    mobile = mobile!!,
                    email = email!!,
                    adharNumber = adharNumber!!,
                    address = address!!,
                    profession = profession!!,
                    accountStatus = AccountStatus(
                        active = false,
                        status = Status.Pending.name
                    )
                )

                val result = professionalInterface.createProfessionalProfile(professionalData)

                if (result) {
                    call.respond(HttpStatusCode.OK, "Profile created successfully")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Error creating profile")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Internal server error: ${e.localizedMessage}")
            }
        }

        get {

            try {

                val result = professionalInterface.getProfessionalsForAdmin()
                call.respond(HttpStatusCode.OK,result)

            }catch (e:Exception){
                call.respond(HttpStatusCode.InternalServerError,e.message.toString())
            }




        }

        patch("/accountStatus/{professionalId}/{active}/{status}") {

            try {

                val active = call.parameters["active"]
                val status = call.parameters["status"]
                val id = call.parameters["professionalId"]

                val result = professionalInterface.updateAccountStatus(
                    id = id.toString(),
                    accountStatus = AccountStatus(active = active.toBoolean(), status = status.toString())
                )
                call.respond(HttpStatusCode.OK,result)


            }catch (e:Exception){

                call.respond(HttpStatusCode.InternalServerError,e.message.toString())

            }


        }

        get("/professionalById/{id}") {

            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed ID")
                return@get
            }

            val professional = professionalInterface.getProfessionalById(id)

            call.respond(HttpStatusCode.OK, professional)
        }

    }
}
