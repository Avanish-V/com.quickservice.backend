package com.example.Repositories.ServiceProducts

import com.example.Model.Rating
import com.example.Model.ReviewDataModel
import com.example.Model.ServiceProductsModel
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.bson.types.ObjectId

class ServiceProduct (db:MongoDatabase): ServiceProductRepository {

    val productCollection = db.getCollection<ServiceProductsModel>("ServiceProducts")
    val reviewCollection = db.getCollection<ReviewDataModel>("Reviews")

    override suspend fun insertProduct(serviceProductsModel: ServiceProductsModel): Boolean {

        return productCollection.insertOne(serviceProductsModel).wasAcknowledged()

    }

    override suspend fun getServiceProduct(id:String): List<ServiceProductsModel> {

        val allProducts = productCollection.find(eq("serviceTAG",id)).toList()

        return allProducts.map { product ->

            val filter = Document("serviceProductId",product.serviceId)
            val productRatings = reviewCollection.find(filter).toList()

            val averageRating = if (productRatings.isNotEmpty()) {
                productRatings.map { it.rating }.average()
            } else {
                4.8
            }
            val ratingCount = if (productRatings.isNotEmpty()) {
                productRatings.map { it.rating }.count()
            } else {
               4
            }


            ServiceProductsModel(
                serviceTitle = product.serviceTitle,
                imageUrl = product.imageUrl,
                serviceId = product.serviceId,
                serviceTAG = product.serviceTAG,
                workType = product.workType,
                price = product.price,
                tax = product.tax,
                description = product.description,
                rating = Rating(
                    rating = String.format("%.1f", averageRating),
                    count = ratingCount.toString(),

                )
            )
        }
    }

    override suspend fun deleteProduct(id: String): Boolean {
        return productCollection.deleteOne(eq("serviceId",id)).wasAcknowledged()
    }
}