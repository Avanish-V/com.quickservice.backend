
import com.example.Authentication.UserDataModel
import com.example.Repositories.Users.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.core.*
import org.slf4j.LoggerFactory


fun Route.userRoute(userRepository: UserRepository) {

    val log = LoggerFactory.getLogger("UserRoute")

    route("/users") {

        get("/userByID/{uuid}") {

            val uuid = call.parameters["uuid"]

            if (uuid == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID is null")
                return@get
            }

            try {
                val result = userRepository.getUserById(uuid)
                if (result != null) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            } catch (e: Exception) {
                log.error("Error fetching user by UUID: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, "Internal server error")
            }
        }

        get {

            try {

                val result = userRepository.getAllUsers()

                if (result.isEmpty()){
                    return@get call.respond(HttpStatusCode.OK,"No users have created!")
                }else{
                    call.respond(HttpStatusCode.OK,result)
                }

            }catch (e:Exception){
                call.respond(HttpStatusCode.InternalServerError,"${e.message}")
            }



        }

        patch("/updateUser") {

            val userData = call.receive<UserDataModel>()

            try {

             val result =   userRepository.updateUser(userData)
             call.respond(result)

            }catch (e:Exception){
                call.respond(e.message.toString())
            }

        }

    }
}


fun validateUserData(userData: UserDataModel) {
    if (userData.userName?.isBlank() == true) {
        throw BadRequestException("Username cannot be empty!")
    }

    if (userData.userGender?.isBlank() == true) {
        throw BadRequestException("Select gender")
    }
}
