package com.example.Repositories.Order

import com.byteapps.serrvicewala.Features.Orders.data.OrdersDataModel
import com.example.Model.ProfessionalById
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.set
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.conversions.Bson

class Orders(db:MongoDatabase):OrderRepository {

    private val orderCollection = db.getCollection<OrdersDataModel>("Orders")


    override suspend fun createOrder(ordersDataModel: OrdersDataModel): Boolean {
        return orderCollection.insertOne(ordersDataModel).wasAcknowledged()
    }

    override suspend fun getOrders(): List<OrdersDataModel> {
        return orderCollection.find().toList()
    }

    override suspend fun getOrdersByUserId(userUID:String): List<OrdersDataModel> {

        val filter = Document("userUUID",userUID)
        return orderCollection.find(filter).toList()
    }

    override suspend fun cancelOrder(orderId: String): Boolean {
        return orderCollection.updateOne(eq("orderId",orderId),set("status","Canceled")).wasAcknowledged()
    }

    override suspend fun getOrderByProfessionalId(professionalId: String):List<OrdersDataModel> {

        return withContext(Dispatchers.IO){

            try {
                val filter: Bson = eq("professionalID", professionalId)
                orderCollection.find(filter).toList()

            } catch (e: Exception) {
                println("Error fetching professional by ID: ${e.localizedMessage}")
                null
            }!!

        }
    }

    override suspend fun confirmAnOrder(orderId: String, professionalId: String): Boolean {

        return withContext(Dispatchers.IO){

            try {
                val filter = Document("orderId", orderId)
                val data = Document("\$set", Document()
                    .append("professionalID", professionalId)
                    .append("status", "Assigned"))
                orderCollection.updateOne(filter,data).wasAcknowledged()

            } catch (e: Exception) {
                println("Error fetching professional by ID: ${e.localizedMessage}")
                null
            }!!

        }
    }


}