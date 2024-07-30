package com.example.Repositories.Order

import com.byteapps.serrvicewala.Features.Orders.data.OrdersDataModel

interface OrderRepository {

    suspend fun createOrder(ordersDataModel: OrdersDataModel):Boolean

    suspend fun getOrders():List<OrdersDataModel>

    suspend fun getOrdersByUserId(userUID:String):List<OrdersDataModel>

    suspend fun cancelOrder(orderId: String) : Boolean

    suspend fun getOrderByProfessionalId(professionalId : String):List<OrdersDataModel>

    suspend fun confirmAnOrder(orderId:String,professionalId : String):Boolean


}