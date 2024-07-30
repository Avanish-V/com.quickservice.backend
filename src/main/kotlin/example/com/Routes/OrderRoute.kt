package com.example.Routes

import com.byteapps.serrvicewala.Features.Orders.data.OrdersDataModel
import com.example.Repositories.Order.OrderRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ordersRoute(orderRepository: OrderRepository){

    route("/orders"){

        post ("/createOrder"){

            try {
                val orders = call.receive<OrdersDataModel>()
                val result = orderRepository.createOrder(orders)

                if (result){
                    call.respond(HttpStatusCode.OK,"Order Created")
                }else{
                    call.respond(HttpStatusCode.InternalServerError,"Failed to create")
                }
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,"Invalid data format")
            }

        }

        get {

            try {
                val ordersData = orderRepository.getOrders()
                call.respond(HttpStatusCode.OK,ordersData)
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,"Invalid data format")
            }

        }

        get ("/{userUID}"){

            val userUID = call.parameters["userUID"]

            try {
                if (userUID != null){
                    val orderByUserList = orderRepository.getOrdersByUserId(userUID)
                    call.respond(HttpStatusCode.OK,orderByUserList)
                }

            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,e.message.toString())
            }

        }

        get ("/{professionalID}"){

            try {
                val professionalId = call.parameters["professionalID"]
                if (professionalId != null){
                    val data = orderRepository.getOrderByProfessionalId(professionalId)
                    call.respond(HttpStatusCode.OK,data)
                }

            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,"Invalid data format")
            }


        }

        patch ("/confirmOrder/{orderId}/{professionalId}"){

            try {

                val orderId = call.parameters["orderId"]
                val professionalId = call.parameters["professionalId"]
                if (orderId == null) return@patch call.respond(HttpStatusCode.BadRequest,"OrderId Missing")
                if (professionalId == null) return@patch call.respond(HttpStatusCode.BadRequest,"professionalId Missing")
                val response = orderRepository.confirmAnOrder(orderId,professionalId)
                call.respond(HttpStatusCode.OK,response)

            }catch (e:Exception){
                call.respond(HttpStatusCode.InternalServerError,"Error:${e.message}")
            }


        }

        patch ("/cancelOrder/{orderId}"){

            val orderId = call.parameters["orderId"]

            try {
                if (orderId != null){
                    val result  = orderRepository.cancelOrder(orderId = orderId)
                    call.respond(HttpStatusCode.OK,result)
                }

            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,"Error: ${e.message}")
            }

        }


    }
}